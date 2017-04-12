package magic.core;

import magic.core.cards.Cards;
import magic.core.cards.DeckBuilder;
import magic.core.cards.lands.BasicLands;
import magic.core.observers.Observer;
import magic.players.RandomPlayer;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
    private final List<Observer> rules;
    private final int numberOfCardsForEachPlayer;
    private final long playerActTimeout;
    private final Random randomState;
    private final List<Set<BasicLands>> deckColors;

    public GameBuilder(int numberOfPlayers, int numberOfCardsForEachPlayer, long playerActTimeout,
                       List<Observer> rules, Random randomState) {
        this(IntStream
                .range(0, numberOfPlayers)
                .mapToObj(i -> new RandomPlayer(Player.DEFAULT_PLAYER_NAMES[i % Player.DEFAULT_PLAYER_NAMES.length]))
                .collect(Collectors.toList()),
            numberOfCardsForEachPlayer,
            playerActTimeout,
            rules,
            randomState);
    }

    public GameBuilder(List<Player> players,
                       int numberOfCardsForEachPlayer, long playerActTimeout,
                       List<Observer> rules, Random randomState) {
        this(players,
            IntStream
                .range(0, players.size())
                .mapToObj(i -> IntStream
                    .range(0, 2)
                    .mapToObj(j -> BasicLands.values()[randomState.nextInt(BasicLands.values().length)])
                    .collect(Collectors.toSet()))
                .collect(Collectors.toList()),
            numberOfCardsForEachPlayer,
            playerActTimeout,
            rules,
            randomState);

    }

    public GameBuilder(List<Player> players, List<Set<BasicLands>> deckColors,
                       int numberOfCardsForEachPlayer, long playerActTimeout,
                       List<Observer> rules, Random randomState) {
        this.players = players;
        this.deckColors = deckColors;
        this.numberOfCardsForEachPlayer = numberOfCardsForEachPlayer;
        this.playerActTimeout = playerActTimeout;
        this.rules = rules;
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
        List<Cards> decks = deckColors.stream().map(b::random).collect(Collectors.toList());
        return new Game(players, decks, playerActTimeout, rules);
    }
}
