package org.jmagic.core;

import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.DeckBuilder;
import org.jmagic.core.cards.lands.BasicLands;
import org.jmagic.observers.Observer;
import org.jmagic.players.Player;

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
    private final List<Set<BasicLands>> deckColors;
    private final List<Observer> observers;
    private final int playersInitialLife;
    private final int numberOfCardsForEachPlayer;
    private final double playerActTimeout;
    private final Random random;

    public GameBuilder(List<Player> players,
                       int playersInitialLife,
                       int numberOfCardsForEachPlayer,
                       double playerActTimeout,
                       List<Observer> observers, Random random) {
        this(players,
            IntStream
                .range(0, players.size())
                .mapToObj(i -> IntStream
                    .range(0, 2)
                    .mapToObj(j -> BasicLands.values()[random.nextInt(BasicLands.values().length)])
                    .collect(Collectors.toSet()))
                .collect(Collectors.toList()),
            playersInitialLife,
            numberOfCardsForEachPlayer,
            playerActTimeout,
            observers,
            random);
    }

    public GameBuilder(List<Player> players, List<Set<BasicLands>> deckColors, int playersInitialLife,
                       int numberOfCardsForEachPlayer, double playerActTimeout,
                       List<Observer> observers, Random random) {
        this.players = players;
        this.deckColors = deckColors;
        this.playersInitialLife = playersInitialLife;
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
        return new Game(players, decks, playersInitialLife, playerActTimeout, observers);
    }
}
