package magic.core;

import magic.core.cards.Cards;
import magic.core.cards.DeckBuilder;
import magic.core.rules.MagicRule;
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
    private final long playerActTimeout;
    private final boolean disqualifyOnInvalidAction;
    private final boolean disqualifyOnIllegalAction;
    private final boolean disqualifyOnActTimeout;
    private final List<MagicRule> rules;
    private final Random randomState;

    public GameBuilder(int numberOfPlayers, int numberOfCardsForEachPlayer, long playerActTimeout,
                       boolean disqualifyOnInvalidAction, boolean disqualifyOnIllegalAction,
                       boolean disqualifyOnActTimeout, List<MagicRule> rules, Random randomState) {
        this(IntStream
                .range(0, numberOfPlayers)
                .mapToObj(i -> new RandomPlayer(Player.DEFAULT_PLAYER_NAMES[i % Player.DEFAULT_PLAYER_NAMES.length]))
                .collect(Collectors.toList()),
            numberOfCardsForEachPlayer,
            playerActTimeout,
            disqualifyOnInvalidAction,
            disqualifyOnIllegalAction,
            disqualifyOnActTimeout,
            rules,
            randomState);
    }

    public GameBuilder(List<Player> players, int numberOfCardsForEachPlayer, long playerActTimeout,
                       boolean disqualifyOnInvalidAction, boolean disqualifyOnIllegalAction,
                       boolean disqualifyOnActTimeout, List<MagicRule> rules, Random randomState) {
        this.players = players;
        this.numberOfCardsForEachPlayer = numberOfCardsForEachPlayer;
        this.playerActTimeout = playerActTimeout;
        this.disqualifyOnInvalidAction = disqualifyOnInvalidAction;
        this.disqualifyOnIllegalAction = disqualifyOnIllegalAction;
        this.disqualifyOnActTimeout = disqualifyOnActTimeout;
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

        List<Cards> playersCards = players.stream()
            .map(p -> b.random())
            .collect(Collectors.toList());

        return new Game(players, playersCards, playerActTimeout,
            disqualifyOnInvalidAction, disqualifyOnIllegalAction,
            disqualifyOnActTimeout, rules);
    }
}
