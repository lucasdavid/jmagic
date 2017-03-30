package magic.core.cards;

import magic.core.State;
import magic.core.contracts.ICard;
import magic.core.contracts.ITargetable;
import magic.core.exceptions.MagicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeckBuilderTest {

    private int numberOfCards;
    private Random random;
    private static final List<ICard> AVAILABLE_CARDS = List.of(
            new MockedCard("mocked-card-1", 4),
            new MockedCard("mocked-card-2", 2),
            new MockedCard("mocked-card-3", 3),
            new MockedCard("mocked-card-4", 4),
            new MockedCard("mocked-card-5", 5),
            new MockedCard("mocked-card-6", 6),
            new MockedCard("mocked-card-7", 7),
            new MockedCard("mocked-card-8", 8),
            new MockedCard("mocked-card-9", 9));

    private static class MockedCard extends Card {

        public MockedCard(UUID id, String name, int cost) {
            super(id, name, cost);
        }

        public MockedCard(String name, int cost) {
            super(UUID.randomUUID(), name, cost);
        }

        @Override
        public State use(State state, List<ITargetable> targets) {
            return null;
        }

        @Override
        public void validUseOrRaisesException(State state, List<ITargetable> targets) throws MagicException {

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
        assertEquals(numberOfCards, deck.getCards().size());

        // Assert all cards are brand new (duplicates).
        assertTrue(deck.getCards().stream().noneMatch(DeckBuilder.AVAILABLE_CARDS::contains));
    }

    @Test
    void testBuildReproducibility() {
        List<ICard> c1 = new DeckBuilder(numberOfCards, new Random(231), AVAILABLE_CARDS).random().getCards();
        List<ICard> c2 = new DeckBuilder(numberOfCards, new Random(231), AVAILABLE_CARDS).random().getCards();

        // Assert that their names and costs match.
        assertTrue(IntStream.range(0, numberOfCards).allMatch(i ->
                c1.get(i).name().equals(c2.get(i).name()) && c1.get(i).cost() == c2.get(i).cost()));
    }
}
