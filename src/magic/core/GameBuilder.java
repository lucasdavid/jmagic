package magic.core;

import magic.core.cards.Cards;
import magic.core.cards.DeckBuilder;
import magic.infrastructure.collectors.CustomCollectors;
import magic.players.RandomPlayer;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Game Builder.
 * <p>
 * Build random games.
 *
 * @author ldavid
 */
public class GameBuilder {

    private final List<Player> players;
    private final int numberOfCardsForEachPlayer;
    private final boolean disqualifyOnInvalidAction;
    private final Random randomState;

    public GameBuilder(int numberOfPlayers, int numberOfCardsForEachPlayer,
                       boolean disqualifyOnInvalidAction, Random randomState) {
        this(IntStream
                        .range(0, numberOfPlayers)
                        .mapToObj(i -> new RandomPlayer(Player.DEFAULT_PLAYER_NAMES[i % Player.DEFAULT_PLAYER_NAMES.length]))
                        .collect(CustomCollectors.toImmutableList()),
                numberOfCardsForEachPlayer,
                disqualifyOnInvalidAction,
                randomState);
    }

    public GameBuilder(List<Player> players, int numberOfCardsForEachPlayer,
                       boolean disqualifyOnInvalidAction, Random randomState) {
        this.players = players;
        this.numberOfCardsForEachPlayer = numberOfCardsForEachPlayer;
        this.disqualifyOnInvalidAction = disqualifyOnInvalidAction;
        this.randomState = randomState;
    }

    /**
     * Generates a random game.
     * <p>
     * The game will have the players passed, each with <param>numberOfCardsForEachPlayer</param>
     * cards.
     *
     * @return a new Game object
     */
    public Game build() {
        DeckBuilder b = new DeckBuilder(numberOfCardsForEachPlayer, randomState);

        List<Cards> playersCards = players.stream()
                .map(p -> b.random())
                .collect(Collectors.toList());

        return new Game(players, playersCards, disqualifyOnInvalidAction);
    }
}
