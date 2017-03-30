package magic.core.cards;

import magic.core.State;
import magic.core.contracts.ICard;
import magic.core.contracts.ITargetable;
import magic.core.exceptions.MagicException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class CardTest {

    class MockedCard extends Card {

        public MockedCard(String name, int cost) {
            this(UUID.randomUUID(), name, cost);
        }

        public MockedCard(UUID id, String name, int cost) {
            super(id, name, cost);
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

    @Test
    void getId() {
        ICard a = new MockedCard("mocked-card-a", 1);
        ICard b = new MockedCard("mocked-card-b", 4);

        assertNotNull(a.id());
        assertNotEquals(a.id(), b.id());

    }

    @Test
    void equals() {
        ICard a = new MockedCard("mocked-card-a", 1);
        assertEquals(a, new MockedCard(a.id(), a.name(), a.cost()));
    }
}
