package org.jmagic.core.states;

import org.jmagic.actions.Action;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.Creature;
import org.jmagic.infrastructure.IDamageable;
import org.jmagic.players.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * State for a Game of org.jmagic.
 * <p>
 * Holds information on the current environment of the game.
 * Properties of interest during a game's execution:
 * <p>
 * {@link State#turn}: the turn in which the game currently isFrom.
 * {@link State#done}: flag whether the game isFrom done or not.
 * {@link State#turnsPlayerIndex}: the id of the turn's current player.
 * {@link State#parent}: the {@link State} that led to this.
 * {@link State#actionThatLedToThisState}: the action that led to this.
 *
 * @author ldavid
 */
public class State {

    public final org.jmagic.core.states.TurnSteps step;
    public final int turn;
    public final int turnsPlayerIndex;
    public final int activePlayerIndex;
    public final boolean done;
    public final State parent;
    public final Action actionThatLedToThisState;
    private final List<PlayerState> playerStates;

    /**
     * Create the initial state for the Hearth Stone game.
     *
     * @param players     a list of players that are in the game.
     * @param playerCards a list of cards for each player in the game.
     */
    public State(List<Player> players, List<Cards> playerCards, int playersInitialLife) {
        this(IntStream
                        .range(0, players.size())
                        .mapToObj(i -> new PlayerState(players.get(i), playerCards.get(i), playersInitialLife))
                        .collect(Collectors.toList()),
                0,
                TurnSteps.values()[0], false, 0, 0, null, null);
    }

    public State(List<PlayerState> playerStates, int turn, TurnSteps step, boolean done,
                 int turnsPlayerIndex, int activePlayerIndex) {
        this(playerStates, turn, step, done, turnsPlayerIndex, activePlayerIndex, null, null);
    }

    /**
     * Create a new state for the Hearth Stone game.
     * <p>
     * State isFrom immutable, which means it cannot be changed. When the state of the game changes with
     * an action, a new State object must be instantiated with this constructor and returned to Game
     * main loop inside {@code Game.validate()} method.
     *
     * @param playerStates     information on the current state of the players in the game
     * @param turn             current turn of the game
     * @param done             flag signaling whether or not the game has finished.
     * @param turnsPlayerIndex the current player's position in the <param>playerStates</param>
     *                         array.
     * @param action           the action applied to reach this current state
     * @param parent           the parent state
     */
    public State(List<PlayerState> playerStates, int turn, TurnSteps step, boolean done,
                 int turnsPlayerIndex, int activePlayerIndex,
                 Action action, State parent) {
        this.turn = turn;
        this.done = done;
        this.turnsPlayerIndex = turnsPlayerIndex;
        this.activePlayerIndex = activePlayerIndex;
        this.playerStates = playerStates;
        this.parent = parent;
        this.actionThatLedToThisState = action;
        this.step = step;
    }

    /**
     * Helper for getting the current player's current state.
     *
     * @return information on the current player of the turn.
     */
    public PlayerState turnsPlayerState() {
        return playerState(turnsPlayerIndex);
    }

    /**
     * Helper for getting the active player's current state.
     *
     * @return information on the player that currently hold control of the game.
     */
    public PlayerState activePlayerState() {
        return playerState(activePlayerIndex);
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
     * {@link State#playerStates} isFrom a mutable array (an therefore a mutable object), which means publicly
     * exposing it would create a capsuling deficiency in the code (the players would be able to
     * freely alter its content). In order to fix this problem, a new array isFrom created.
     * Additionally, there's no need to copy each PlayerState object inside the array, as they are
     * immutable.
     *
     * @return a copy of the information on the current state of all players in the game.
     */
    public List<PlayerState> playerStates() {
        return new ArrayList<>(playerStates);
    }

    public State playerViewModel(Player player) {
        return playerViewModel(playerState(player));
    }

    private State playerViewModel(PlayerState playerState) {
        List<PlayerState> players = playerStates.stream().map(
                _p -> _p.equals(playerState)
                        ? _p.playerViewModel()
                        : _p.opponentViewModel()).collect(Collectors.toList());

        return new State(players, turn, step, done, turnsPlayerIndex,
                activePlayerIndex, actionThatLedToThisState,
                parent == null ? null : parent.playerViewModel(playerState));
    }

    @Override
    public String toString() {
        return String.format("turn: %d%s, %s, players:\n%s",
                turn, (done ? " (done)" : ""),
                step,
                String.join(
                        "\n",
                        playerStates.stream()
                                .map(PlayerState::toString)
                                .collect(Collectors.toList())));
    }

    /**
     * Player State.
     * <p>
     * Holds information on a player's current status.
     */
    public static class PlayerState implements IDamageable {

        public final Player player;
        public final Cards deck, hand, field, graveyard;
        public final boolean playing;
        public final Map<Creature, Player> attackers;
        public final Map<Creature, Creature> blockers;
        private final int life;
        private final int originalLife;

        PlayerState(Player player, Cards deck, int initialLife) {
            this(player, initialLife, initialLife, deck, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.emptyMap(), Collections.emptyMap(), true);
        }

        public PlayerState(Player player, int life, int originalLife, Cards deck, Cards hand, Cards field, Cards graveyard,
                           Map<Creature, Player> attackers, Map<Creature, Creature> blockers, boolean playing) {
            this.player = player;
            this.life = life;
            this.originalLife = originalLife;
            this.deck = deck;
            this.hand = hand;
            this.field = field;
            this.graveyard = graveyard;

            this.attackers = Collections.unmodifiableMap(attackers);
            this.blockers = Collections.unmodifiableMap(blockers);
            this.playing = playing;
        }

        @Override
        public PlayerState takeDamage(int damage) {
            return new PlayerState(player, life - damage, originalLife,
                    deck, hand, field, graveyard, attackers, blockers, playing);
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
        public int originalLife() {
            return originalLife;
        }

        @Override
        public int effectiveOriginalLife() {
            return originalLife();
        }

        @Override
        public boolean isAlive() {
            return effectiveLife() > 0 && playing;
        }

        private PlayerState playerViewModel() {
            return new PlayerState(player, life, originalLife, null,
                    hand, field, graveyard, attackers, blockers, playing);
        }

        private PlayerState opponentViewModel() {
            return new PlayerState(player, life, originalLife, null, null,
                    field, graveyard, attackers, blockers, playing);
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
            return String.format("%s:\n" +
                            "  life: %d/%d,\n" +
                            "  hand: %s,\n" +
                            "  field: %s,\n" +
                            "  deck: %s,\n" +
                            "  graveyard: %s\n" +
                            "  attackers: %s\n" +
                            "  blockers: %s",
                    player, effectiveLife(), effectiveOriginalLife(), hand, field, deck, graveyard,
                    attackers, blockers);
        }
    }
}
