package magic.core;

import magic.core.State.PlayerInfo;
import magic.core.actions.*;
import magic.core.cards.Cards;
import magic.core.cards.DeckBuilder;
import magic.core.exceptions.MagicException;
import magic.core.exceptions.IllegalActionException;
import magic.core.exceptions.InvalidActionException;
import magic.infrastructure.collectors.CustomCollectors;
import magic.players.RandomPlayer;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Game.
 * <p>
 * Defines a Game (a box with players, winners and a state history) and
 * protects it using privacy.
 *
 * @author ldavid
 */
public class Game {

    public static final Logger LOG = Logger.getLogger(Game.class.getName());

    public final static Collection<Class<? extends Action>> LEGAL_ACTIONS_FOR_PLAYERS = Set.of(
            DrawAction.class, PassAction.class, PlayAction.class);

    private final boolean disqualifyOnInvalidAction;
    private final UUID id;

    private Date startedAt;
    private Date finishedAt;
    private State currentState;
    private Collection<Player> players;
    private Collection<Player> winners;

    public Game(List<Player> players, List<Cards> playersCards, boolean disqualifyOnInvalidAction) {
        this(UUID.randomUUID(), players, new State(players, playersCards), disqualifyOnInvalidAction);
    }

    public Game(List<Player> players, State initialState, boolean disqualifyOnInvalidAction) {
        this(UUID.randomUUID(), players, initialState, disqualifyOnInvalidAction);
    }

    public Game(UUID id, List<Player> players, State initialState, boolean disqualifyOnInvalidAction) {
        this.id = id;
        this.players = players;
        this.currentState = initialState;
        this.disqualifyOnInvalidAction = disqualifyOnInvalidAction;
    }

    public Game run() {
        this.startedAt = new Date();

        LOG.log(Level.INFO, "Initial state: {0}", currentState);

        while (!currentState.done) {
            PlayerInfo playerInfo = currentState.currentPlayerInfo();

            try {
                Action action = playerInfo.player.act(currentState.playerViewModel());

                if (!LEGAL_ACTIONS_FOR_PLAYERS.contains(action.getClass())) {
                    throw new IllegalActionException("action " + action + "is not defined as legal by the game rules");
                }

                if (action == null) {
                    throw new InvalidActionException("actions cannot be null");
                }

                action.raiseForErrors(currentState);

                currentState = action.update(currentState);

            } catch (InvalidActionException ex) {
                // This player's attempted an invalid action.
                LOG.log(Level.WARNING, null, ex);
                if (!passAndMaybeFinish(disqualifyOnInvalidAction)) {
                    return this;
                }

            } catch (IllegalActionException ex) {
                // This player's attempted to use an illegal action. Disqualify them.
                LOG.log(Level.SEVERE, null, ex);
                if (!passAndMaybeFinish(true)) {
                    return this;
                }

            } catch (MagicException ex) {
                // A very serious exception has been raised. Stops the game altogether.
                LOG.log(Level.SEVERE, null, ex);
                currentState = new FinishGameAction().update(currentState);
                this.finishedAt = new Date();
                return this;
            }
        }

        winners = currentState.getPlayersInfo().stream()
                .filter(i -> i.playing && i.life > 0)
                .map(i -> i.player)
                .collect(CustomCollectors.toImmutableList());

        this.finishedAt = new Date();
        return this;
    }

    /**
     * @param disqualifyCurrentPlayer flag whether or not the current player
     *                                should be disqualified.
     * @return true if the game has passed onto the next player.
     * False if there's no other player to pass the game onto.
     */
    private boolean passAndMaybeFinish(boolean disqualifyCurrentPlayer) {
        if (disqualifyCurrentPlayer) {
            currentState = new DisqualifyAction(currentState.turnsCurrentPlayerId)
                    .update(currentState);
        }

        boolean passing = new PassAction().isValid(currentState);
        if (passing) {
            currentState = new PassAction().update(currentState);
        } else {
            currentState = new FinishGameAction().update(currentState);
            finishedAt = new Date();
        }
        return passing;
    }

    /**
     * Generate a random game.
     *
     * @param numberOfPlayers            number of players in the new randomly generated game.
     * @param numberOfCardsForEachPlayer number of random cards distributed to each player.
     * @param seed:                      the seed used to create a new <code>Random</code> state.
     * @return new Game object.
     */
    public static Game random(int numberOfPlayers, int numberOfCardsForEachPlayer,
                              boolean disqualifyOnInvalidAction, int seed) {
        return random(numberOfPlayers, numberOfCardsForEachPlayer, disqualifyOnInvalidAction, new Random(seed));
    }

    /**
     * Generate a random game.
     * <p>
     * The game will have <param>numberOfPlayers</param> players, each withal
     * <param>numberOfCardsForEachPlayer</param> cards.
     *
     * @param numberOfPlayers            number of players in the new randomly generated game.
     * @param numberOfCardsForEachPlayer number of random cards distributed to each player.
     * @param randomState                a random state used for reproducibility.
     * @return new Game object
     */
    public static Game random(int numberOfPlayers, int numberOfCardsForEachPlayer,
                              boolean disqualifyOnInvalidAction, Random randomState) {
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new RandomPlayer(Player.DEFAULT_PLAYER_NAMES[i % Player.DEFAULT_PLAYER_NAMES.length]));
        }

        return random(players, numberOfCardsForEachPlayer, disqualifyOnInvalidAction, randomState);
    }

    /**
     * Generate a random game.
     * <p>
     * The game will have the players passed, each with <param>numberOfCardsForEachPlayer</param>
     * cards.
     *
     * @param players                    the players in the new randomly generated game.
     * @param numberOfCardsForEachPlayer number of random cards distributed to each player.
     * @param randomState                a random state used for reproducibility.
     * @return a new Game object
     */
    public static Game random(List<Player> players, int numberOfCardsForEachPlayer,
                              boolean disqualifyOnInvalidAction, Random randomState) {
        DeckBuilder b = new DeckBuilder(numberOfCardsForEachPlayer, randomState);

        List<Cards> playersCards = players.stream()
                .map(p -> b.random())
                .collect(Collectors.toList());

        return new Game(players, playersCards, disqualifyOnInvalidAction);
    }

    @Override
    public String toString() {
        return String.format("Game #%d's players: %s", id, players)
                + (winners != null && !winners.isEmpty()
                ? String.format(" winners: %s", winners)
                : "");
    }

    public Date startedAt() {
        return (Date) this.startedAt.clone();
    }

    public Date finishedAt() {
        return (Date) this.finishedAt.clone();
    }

    public Collection<Player> winners() {
        return winners;
    }
}
