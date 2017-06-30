package org.games.jmagic.core;

import org.games.jmagic.core.cards.Cards;
import org.games.jmagic.core.cards.DeckBuilder;
import org.games.jmagic.core.cards.lands.BasicLands;
import org.games.jmagic.observers.Observer;
import org.games.jmagic.players.Player;

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
    private final List<Observer> observers;
    private final int numberOfCardsForEachPlayer;
    private final long playerActTimeout;
    private final Random random;
    private final List<Set<BasicLands>> deckColors;

    public GameBuilder(List<Player> players,
                       int numberOfCardsForEachPlayer, long playerActTimeout,
                       List<Observer> observers, Random random) {
        this(players,
            IntStream
                .range(0, players.size())
                .mapToObj(i -> IntStream
                    .range(0, 2)
                    .mapToObj(j -> BasicLands.values()[random.nextInt(BasicLands.values().length)])
                    .collect(Collectors.toSet()))
                .collect(Collectors.toList()),
            numberOfCardsForEachPlayer,
            playerActTimeout,
            observers,
            random);
    }

    public GameBuilder(List<Player> players, List<Set<BasicLands>> deckColors,
                       int numberOfCardsForEachPlayer, long playerActTimeout,
                       List<Observer> observers, Random random) {
        this.players = players;
        this.deckColors = deckColors;
        this.numberOfCardsForEachPlayer = numberOfCardsForEachPlayer;
        this.playerActTimeout = playerActTimeout;
        this.observers = observers;
        this.random = random;
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
        DeckBuilder b = new DeckBuilder(numberOfCardsForEachPlayer, random);
        List<Cards> decks = deckColors.stream()
            .map(b::random)
            .collect(Collectors.toList());
        return new Game(players, decks, playerActTimeout, observers);
    }
}
