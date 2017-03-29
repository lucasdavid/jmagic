package hearthstone.core;

import hearthstone.core.actions.Action;
import hearthstone.core.contracts.IDamageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * State for a HearthStone Game.
 * <p>
 * Holds information on the current environment of the game.
 * Properties of interest during a game's execution:
 * <p>
 * {@link State#turn}: the turn in which the game currently is.
 * {@link State#done}: flag whether the game is done or not.
 * {@link State#turnsCurrentPlayerId}: the id of the turn's current player.
 * {@link State#parent}: the {@link State} that led to this.
 * {@link State#actionThatLedToThisState}: the action that led to this.
 *
 * @author ldavid
 */
public class State {

    /**
     * Player Information.
     * <p>
     * Holds information on the players' current statuses.
     */
    public static class PlayerInfo implements IDamageable {

        public static final int DEFAULT_INITIAL_LIFE = 20;

        public final int life;
        public final int maxLife;

        public final Player player;
        public final Cards deck, hand, field, graveyard;
        public final boolean playing;

        PlayerInfo(Player player, Cards deck) {
            this(player, DEFAULT_INITIAL_LIFE, DEFAULT_INITIAL_LIFE, deck, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY);
        }

        public PlayerInfo(Player player, int life, int maxLife, Cards deck, Cards hand, Cards field, Cards graveyard) {
            this(player, life, maxLife, deck, hand, field, graveyard, true);
        }

        public PlayerInfo(Player player, int life, int maxLife, Cards deck, Cards hand, Cards field, Cards graveyard,
                          boolean playing) {
            this.player = player;
            this.life = life;
            this.maxLife = maxLife;
            this.deck = deck;
            this.hand = hand;
            this.field = field;
            this.graveyard = graveyard;
            this.playing = playing;
        }

        @Override
        public IDamageable takeDamage(int damage) {
            return new PlayerInfo(player, life - damage, maxLife, deck, hand, field, graveyard);
        }

        @Override
        public int getLife() {
            return life;
        }

        @Override
        public int getMaxLife() {
            return maxLife;
        }

        @Override
        public String toString() {
            return String.format("%s {\n  life: %d,\n  hand: %s,\n  deck: %s\n}", player, life,
                    hand, deck);
        }

        private PlayerInfo opponentViewModel() {
            return new PlayerInfo(player, life, maxLife, null, null, field, graveyard);
        }

        private PlayerInfo playerViewModel() {
            return new PlayerInfo(player, life, maxLife, null, hand, field, graveyard);
        }
    }

    public final int turn;
    public final boolean done;
    public final int turnsCurrentPlayerId;
    private final List<PlayerInfo> playersInfo;
    public final State parent;
    public final Action actionThatLedToThisState;

    /**
     * Create the initial state for the Hearth Stone game.
     *
     * @param players     a list of players that are in the game.
     * @param playerCards a list of cards for each player in the game.
     */
    public State(List<Player> players, List<Cards> playerCards) {
        List<PlayerInfo> playersInfo = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            playersInfo.add(new PlayerInfo(players.get(i), playerCards.get(i)));
        }

        this.turn = 0;
        this.done = false;
        this.turnsCurrentPlayerId = 0;
        this.playersInfo = playersInfo;
        this.parent = null;
        this.actionThatLedToThisState = null;
    }

    public State(List<PlayerInfo> playersInfo, int turn, boolean done,
                 int turnsCurrentPlayerId) {
        this(playersInfo, turn, done, turnsCurrentPlayerId, null, null);
    }

    /**
     * Create a new state for the Hearth Stone game.
     * <p>
     * State is immutable, which means it cannot be changed. When the state of the game changes with
     * an action, a new State object must be instantiated with this constructor and returned to Game
     * main loop inside {@code Game.run()} method.
     *
     * @param playersInfo          information on the current state of the players in the game
     * @param turn                 current turn of the game
     * @param done                 flag signaling whether or not the game has finished.
     * @param turnsCurrentPlayerId the current player's position in the <param>playersInfo</param>
     *                             array.
     * @param action               the action applied to reach this current state
     * @param parent               the parent state
     */
    public State(List<PlayerInfo> playersInfo, int turn, boolean done,
                 int turnsCurrentPlayerId, Action action, State parent) {
        this.turn = turn;
        this.done = done;
        this.turnsCurrentPlayerId = turnsCurrentPlayerId;
        this.playersInfo = playersInfo;
        this.parent = parent;
        this.actionThatLedToThisState = action;
    }

    /**
     * Helper for getting the current player's information.
     *
     * @return information on the player that currently hold control of the game.
     */
    public PlayerInfo currentPlayerInfo() {
        return playerInfo(turnsCurrentPlayerId);
    }

    public PlayerInfo playerInfo(int playerId) {
        return playersInfo.get(playerId);
    }

    /**
     * Retrieve information on all players in the game.
     * <p>
     * {@link State#playersInfo} is a mutable array (an therefore a mutable object), which means publicly
     * exposing it would create a capsuling deficiency in the code (the players would be able to
     * freely alter its content). In order to fix this problem, a new array is created.
     * Additionally, there's no need to copy each PlayerInfo object inside the array, as they are
     * immutable.
     *
     * @return a copy of the information on the current state of all players in the game.
     */
    public List<PlayerInfo> getPlayersInfo() {
        return new ArrayList<>(playersInfo);
    }

    State playerViewModel() {
        return playerViewModel(turnsCurrentPlayerId);
    }

    State playerViewModel(int playerId) {
        PlayerInfo _p = playerInfo(playerId);

        List<PlayerInfo> players = playersInfo.stream().map(
                info -> info.equals(_p)
                        ? info.playerViewModel()
                        : info.opponentViewModel()).collect(Collectors.toList());

        return new State(players, turn, done, turnsCurrentPlayerId, actionThatLedToThisState,
                parent == null ? null : parent.playerViewModel(playerId));
    }

    @Override
    public String toString() {
        return String.format("turn: %d, players:\n %s", turn,
                String.join("\n", playersInfo.stream()
                        .map(PlayerInfo::toString)
                        .collect(Collectors.toList())))
                + (done ? "(done)" : "");
    }
}
