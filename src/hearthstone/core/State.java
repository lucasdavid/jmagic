package hearthstone.core;

import hearthstone.core.actions.Action;
import hearthstone.core.cards.Cards;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * State for a HearthStone Game.
 *
 * Holds information on the current environment of the game.
 *
 * @author ldavid
 */
public class State {

    public static class PlayerInfo {

        public static final int DEFAULT_INITIAL_LIFE = 20;

        public final int life;
        public final Player player;
        public final Cards deck, hand, field, graveyard;

        PlayerInfo(Player player, Cards deck) {
            this(player, DEFAULT_INITIAL_LIFE, deck, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY);
        }

        PlayerInfo(PlayerInfo playerInfo) {
            this(playerInfo.player, playerInfo.life, playerInfo.deck,
                    playerInfo.hand, playerInfo.field, playerInfo.graveyard);
        }

        public PlayerInfo(Player player, int life, Cards deck, Cards hand, Cards field, Cards graveyard) {
            this.player = player;
            this.life = life;
            this.deck = deck;
            this.hand = hand;
            this.field = field;
            this.graveyard = graveyard;
        }

        @Override
        public String toString() {
            return String.format("%s {\n  life: %d,\n  hand: %s,\n  deck: %s\n}", player, life,
                    hand, deck);
        }

        private PlayerInfo opponentViewModel() {
            return new PlayerInfo(player, life, null, null, field, graveyard);
        }

        private PlayerInfo currentPlayerViewModel() {
            return new PlayerInfo(player, life, null, hand, field, graveyard);
        }
    }

    public final int turn;
    public final boolean done;
    public final State parent;
    public final int turnsCurrentPlayerId;
    public final Action actionThatLedToThisState;
    private final List<PlayerInfo> playerInfos;

    /**
     * Create the initial state for the Hearth Stone game.
     *
     * @param players a list of players that are in the game.
     * @param playerCards a list of cards for each player in the game.
     */
    public State(List<Player> players, List<Cards> playerCards) {
        List<PlayerInfo> info = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            info.add(new PlayerInfo(players.get(i), playerCards.get(i)));
        }

        this.turn = 0;
        this.done = false;
        this.turnsCurrentPlayerId = 0;
        this.playerInfos = info;
        this.parent = null;
        this.actionThatLedToThisState = null;
    }

    /**
     * Create a new state for the Hearth Stone game.
     *
     * State is immutable, which means it cannot be changed. When the state of the game changes with
     * an action, a new State object must be instantiated with this constructor and returned to Game
     * main loop inside {@code Game.run()} method.
     *
     * @param playerInfos information on the current state of the players in the game
     * @param turn current turn of the game
     * @param done flag signaling whether or not the game has finished.
     * @param turnsCurrentPlayerId the current player's position in the <param>playersInfo</param>
     * array.
     * @param action the action applied to reach this current state
     * @param parent the parent state
     */
    public State(List<PlayerInfo> playerInfos, int turn, boolean done,
            int turnsCurrentPlayerId, Action action, State parent) {
        this.turn = turn;
        this.done = done;
        this.turnsCurrentPlayerId = turnsCurrentPlayerId;
        this.playerInfos = playerInfos;
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
        return playerInfos.get(playerId);
    }

    /**
     * Retrieve information on all players in the game.
     *
     * {@code playerInfos} is a mutable array (an therefore a mutable object), which means publicly
     * exposing it would create a capsuling deficiency in the code (the players would be able to
     * freely alter its content). In order to fix this problem, a new array is created.
     * Additionally, there's no need to copy each PlayerInfo object inside the array, as they are
     * immutable.
     *
     * @return a copy of the information on the current state of all players in the game.
     */
    public List<PlayerInfo> getPlayerInfos() {
        return new ArrayList<>(playerInfos);
    }

    State currentPlayerViewModel() {
        return currentPlayerViewModel(turnsCurrentPlayerId);
    }

    State currentPlayerViewModel(int playerId) {
        PlayerInfo _p = playerInfo(playerId);

        List<PlayerInfo> _playerInfos = playerInfos.stream().map(
                info -> info.equals(_p)
                ? info.currentPlayerViewModel()
                : info.opponentViewModel()).collect(Collectors.toList());

        return new State(_playerInfos, turn, done, turnsCurrentPlayerId, actionThatLedToThisState,
                parent == null ? null : parent.currentPlayerViewModel(playerId));
    }

    @Override
    public String toString() {
        return String.format("turn: %d, players:\n %s", turn,
                String.join("\n", playerInfos.stream().map(e -> e.toString()).collect(Collectors.toList())))
                + (done ? "(done)" : "");
    }
}
