package org.games.jmagic.core.cards;

import org.games.jmagic.core.cards.lands.BasicLands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeckBuilderTest {

    private int numberOfCards;
    private Random random;
    private static final List<ICard> AVAILABLE_CARDS = List.of(
            new MockedCard("mocked-card-1", List.of(BasicLands.PLAINS)),
            new MockedCard("mocked-card-2", List.of(BasicLands.MOUNTAIN, BasicLands.FOREST)),
            new MockedCard("mocked-card-3", List.of(BasicLands.PLAINS, BasicLands.MOUNTAIN)),
            new MockedCard("mocked-card-4", List.of(BasicLands.SWAMP, BasicLands.ISLAND)),
            new MockedCard("mocked-card-5", List.of(BasicLands.PLAINS, BasicLands.FOREST, BasicLands.WASTES)),
            new MockedCard("mocked-card-6", List.of(BasicLands.SWAMP)),
            new MockedCard("mocked-card-7", List.of(BasicLands.FOREST, BasicLands.MOUNTAIN)),
            new MockedCard("mocked-card-8", List.of(BasicLands.ISLAND, BasicLands.SWAMP)),
            new MockedCard("mocked-card-9", List.of(BasicLands.PLAINS, BasicLands.PLAINS)));

    private static class MockedCard extends Card {

        MockedCard(UUID id, String name, Collection<BasicLands> cost) {
            super(id, name, Collections.emptyList(), cost);
        }

        MockedCard(String name, Collection<BasicLands> cost) {
            super(UUID.randomUUID(), name, Collections.emptyList(), cost);
        }

        @Override
        public ICard duplicate() {
            return new MockedCard(name(), cost());
        }
    }

    @BeforeEach
    void beforeEach() {
        this.numberOfCards = 5;
        this.random = new Random(231);
    }

    @Test
    void testBuild() {
        DeckBuilder b = new DeckBuilder(numberOfCards, random, AVAILABLE_CARDS);
        Cards deck = b.random();

        // Assert the deck has `numberOfCards` cards.
        assertEquals(numberOfCards, deck.cards().size());

        // Assert all cards are brand new (duplicates).
        assertTrue(deck.cards().stream().noneMatch(DeckBuilder.AVAILABLE_CARDS::contains));
    }

    @Test
    void testBuildReproducibility() {
        List<ICard> c1 = new DeckBuilder(numberOfCards, new Random(231), AVAILABLE_CARDS).random().cards();
        List<ICard> c2 = new DeckBuilder(numberOfCards, new Random(231), AVAILABLE_CARDS).random().cards();

        // Assert that their names and costs match.
        for (int i = 0; i < numberOfCards; i++) {
            assertTrue(c1.get(i).name().equals(c2.get(i).name())
                    && c1.get(i).cost().equals(c2.get(i).cost()));
        }
    }
}
