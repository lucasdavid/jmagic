package hearthstone.core;

import hearthstone.core.State.PlayerInfo;
import hearthstone.core.actions.Action;
import hearthstone.core.cards.Cards;
import hearthstone.core.exceptions.HearthStoneException;
import hearthstone.core.exceptions.InvalidActionException;
import hearthstone.core.exceptions.PlayerException;
import hearthstone.infrastructure.collectors.ImmutableListCollector;
import hearthstone.players.RandomPlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author ldavid
 */
public class Game {

    public static final Logger LOG = Logger.getLogger(Game.class.getName());

    private UUID id;
    private Date startedAt;
    private Date finishedAt;
    private State currentState;
    private Collection<Player> players;
    private Collection<Player> winners;

    public Game(List<Player> players, List<Cards> playersCards) {
        this(UUID.randomUUID(), players, playersCards);
    }

    public Game(UUID id, List<Player> players, List<Cards> playersCards) {
        this(id, players, new State(players, playersCards));
    }

    public Game(List<Player> players, State initialState) {
        this(UUID.randomUUID(), players, initialState);
    }

    public Game(UUID id, List<Player> players, State initialState) {
        this.id = id;
        this.players = players;
        this.currentState = initialState;
    }

    public Game run() {
        this.startedAt = new Date();

        LOG.log(Level.INFO, "Initial state: {0}", currentState);

        while (!currentState.done) {
            PlayerInfo playerInfo = currentState.turnsCurrentPlayerInfo();

            try {
                Action action = playerInfo.player.act(currentState);

                if (action == null) {
                    throw new InvalidActionException("actions cannot be null");
                }

                action.validActionOrRaisesException(currentState);

                currentState = action.update(currentState);

            } catch (PlayerException ex) {
                // This player's thrown a very problematic error. Define all other players
                // as winners and keep playing.
                LOG.log(Level.SEVERE, null, ex);
                currentState = new State(currentState, true);
                winners = players.stream()
                        .filter(p -> !p.equals(playerInfo.player))
                        .collect(Collectors.toList());

            } catch (HearthStoneException ex) {
                LOG.log(Level.SEVERE, null, ex);
                currentState = new State(currentState, true);
            }
        }

        winners = currentState.getPlayerInfos().stream()
                .filter(i -> i.life > 0)
                .map(i -> i.player)
                .collect(ImmutableListCollector.toImmutableList());

        this.finishedAt = new Date();
        return this;
    }

    /**
     * Generate a random game.
     *
     * @param numberOfPlayers
     * @param numberOfCardsForEachPlayer
     * @param seed: the seed used to create a new <code>Random</code> state.
     * @return new Game object.
     */
    public static Game random(int numberOfPlayers, int numberOfCardsForEachPlayer, int seed) {
        return random(numberOfPlayers, numberOfCardsForEachPlayer, new Random(seed));
    }

    /**
     * Generate a random game.
     *
     * The game will have <param>numberOfPlayers</param> players, each withal
     * <param>numberOfCardsForEachPlayer</param> cards.
     *
     * @param numberOfPlayers
     * @param numberOfCardsForEachPlayer
     * @param randomState
     * @return new Game object
     */
    public static Game random(int numberOfPlayers, int numberOfCardsForEachPlayer, Random randomState) {
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new RandomPlayer(Player.DEFAULT_PLAYER_NAMES[i % Player.DEFAULT_PLAYER_NAMES.length]));
        }

        return random(players, numberOfCardsForEachPlayer, randomState);
    }

    /**
     * Generate a random game.
     *
     * The game will have the players passed, each with <param>numberOfCardsForEachPlayer</param>
     * cards.
     *
     * @param players
     * @param numberOfCardsForEachPlayer
     * @param randomState
     * @return a new Game object
     */
    public static Game random(List<Player> players, int numberOfCardsForEachPlayer, Random randomState) {
        List<Cards> playersCards = new ArrayList<>();
        players.forEach((p) -> playersCards.add(Cards.random(numberOfCardsForEachPlayer, randomState)));
        return new Game(players, playersCards);
    }

    @Override
    public String toString() {
        return String.format("Game #%d's players: %s", id, players)
                + (winners != null && !winners.isEmpty()
                ? String.format(" winners: %s", winners)
                : "");
    }

    public Date getStartedAt() {
        return (Date) this.startedAt.clone();
    }

    public Date getFinishedAt() {
        return (Date) this.finishedAt.clone();
    }

    public State getCurrentState() {
        return currentState;
    }

    public Collection<Player> getWinner() {
        return winners;
    }
}
