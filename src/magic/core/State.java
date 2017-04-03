package magic.core;

import magic.core.actions.Action;
import magic.core.cards.Cards;
import magic.core.contracts.IDamageable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * State for a HearthStone Game.
 * <p>
 * Holds information on the current environment of the game.
 * Properties of interest during a game's execution:
 * <p>
 * {@link State#turn}: the turn in which the game currently is.
 * {@link State#done}: flag whether the game is done or not.
 * {@link State#turnsCurrentPlayerIndex}: the id of the turn's current player.
 * {@link State#parent}: the {@link State} that led to this.
 * {@link State#actionThatLedToThisState}: the action that led to this.
 *
 * @author ldavid
 */
public class State {

    /**
     * Player State.
     * <p>
     * Holds information on a player's current status.
     */
    public static class PlayerState implements IDamageable {

        public static final int DEFAULT_INITIAL_LIFE = 20;

        public final int life;
        public final int maxLife;

        public final Player player;
        public final Cards deck, hand, field, graveyard;
        public final boolean playing;

        PlayerState(Player player, Cards deck) {
            this(player, DEFAULT_INITIAL_LIFE, DEFAULT_INITIAL_LIFE, deck, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY);
        }

        public PlayerState(Player player, int life, int maxLife, Cards deck, Cards hand, Cards field, Cards graveyard) {
            this(player, life, maxLife, deck, hand, field, graveyard, true);
        }

        public PlayerState(Player player, int life, int maxLife, Cards deck, Cards hand, Cards field, Cards graveyard,
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
            return new PlayerState(player, life - damage, maxLife, deck, hand, field, graveyard);
        }

        @Override
        public int life() {
            return life;
        }

        @Override
        public int effectiveLife() {
            return life();
        }

        @Override
        public int maxLife() {
            return maxLife;
        }

        @Override
        public int effectiveMaxLife() {
            return maxLife();
        }

        @Override
        public boolean isAlive() {
            return effectiveLife() > 0 && playing;
        }

        private PlayerState opponentViewModel() {
            return new PlayerState(player, life, maxLife, null, null, field, graveyard);
        }

        private PlayerState playerViewModel() {
            return new PlayerState(player, life, maxLife, null, hand, field, graveyard);
        }

        @Override
        public boolean equals(Object o) {
            try {
                return player.equals(((PlayerState) o).player);
            } catch (ClassCastException | NullPointerException ex) {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return player.hashCode();
        }

        @Override
        public String toString() {
            return String.format("%s {\n  life: %d/%d,\n  hand: %s,\n  field: %s,\n" +
                            "  deck: %s,\n  graveyard: %s\n}",
                    player, effectiveLife(), effectiveMaxLife(), hand, field, deck, graveyard);
        }
    }

    public final int turn;
    public final boolean done;
    public final int turnsCurrentPlayerIndex;
    private final List<PlayerState> playerStates;
    public final State parent;
    public final Action actionThatLedToThisState;

    /**
     * Create the initial state for the Hearth Stone game.
     *
     * @param players     a list of players that are in the game.
     * @param playerCards a list of cards for each player in the game.
     */
    public State(List<Player> players, List<Cards> playerCards) {
        this.turn = 0;
        this.done = false;
        this.turnsCurrentPlayerIndex = 0;
        this.playerStates = IntStream
                .range(0, players.size())
                .mapToObj(i -> new PlayerState(players.get(i), playerCards.get(i)))
                .collect(Collectors.toList());

        this.parent = null;
        this.actionThatLedToThisState = null;
    }

    /**
     * Create a new state for the Hearth Stone game.
     * <p>
     * State is immutable, which means it cannot be changed. When the state of the game changes with
     * an action, a new State object must be instantiated with this constructor and returned to Game
     * main loop inside {@code Game.validate()} method.
     *
     * @param playerStates            information on the current state of the players in the game
     * @param turn                    current turn of the game
     * @param done                    flag signaling whether or not the game has finished.
     * @param turnsCurrentPlayerIndex the current player's position in the <param>playerStates</param>
     *                                array.
     * @param action                  the action applied to reach this current state
     * @param parent                  the parent state
     */
    public State(List<PlayerState> playerStates, int turn, boolean done,
                 int turnsCurrentPlayerIndex, Action action, State parent) {
        this.turn = turn;
        this.done = done;
        this.turnsCurrentPlayerIndex = turnsCurrentPlayerIndex;
        this.playerStates = playerStates;
        this.parent = parent;
        this.actionThatLedToThisState = action;
    }

    /**
     * Helper for getting the current player's current state.
     *
     * @return information on the player that currently hold control of the game.
     */
    public PlayerState currentPlayerState() {
        return playerState(turnsCurrentPlayerIndex);
    }

    public PlayerState playerState(int playerIndex) {
        return playerStates.get(playerIndex);
    }

    public PlayerState playerState(Player player) {
        return playerStates.stream()
                .filter(i -> i.player.equals(player))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("unknown player " + player));
    }

    /**
     * Retrieve information on all players in the game.
     * <p>
     * {@link State#playerStates} is a mutable array (an therefore a mutable object), which means publicly
     * exposing it would create a capsuling deficiency in the code (the players would be able to
     * freely alter its content). In order to fix this problem, a new array is created.
     * Additionally, there's no need to copy each PlayerState object inside the array, as they are
     * immutable.
     *
     * @return a copy of the information on the current state of all players in the game.
     */
    public List<PlayerState> playerStates() {
        return new ArrayList<>(playerStates);
    }

    State playerViewModel() {
        return playerViewModel(turnsCurrentPlayerIndex);
    }

    State playerViewModel(int playerId) {
        PlayerState _p = playerState(playerId);

        List<PlayerState> players = playerStates.stream().map(
                playerState -> playerState.equals(_p)
                        ? playerState.playerViewModel()
                        : playerState.opponentViewModel()).collect(Collectors.toList());

        return new State(players, turn, done, turnsCurrentPlayerIndex, actionThatLedToThisState,
                parent == null ? null : parent.playerViewModel(playerId));
    }

    @Override
    public String toString() {
        return String.format("turn: %d, players:\n %s", turn,
                String.join("\n", playerStates.stream()
                        .map(PlayerState::toString)
                        .collect(Collectors.toList())))
                + (done ? "(done)" : "");
    }
}
