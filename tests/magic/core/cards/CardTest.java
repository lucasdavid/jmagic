package magic.core.cards;

import magic.core.cards.lands.BasicLands;
import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class CardTest {

    private class MockedCard extends Card {

        MockedCard(String name, Collection<BasicLands> cost) {
            this(UUID.randomUUID(), name, cost);
        }

        MockedCard(UUID id, String name, Collection<BasicLands> cost) {
            super(id, name, Collections.emptyList(), cost);
        }

        @Override
        public ICard duplicate() {
            return new MockedCard(name(), cost());
        }
    }

    @Test
    void getId() {
        ICard a = new MockedCard("mocked-card-a", List.of(BasicLands.PLAINS));
        ICard b = new MockedCard("mocked-card-b", List.of(BasicLands.ISLAND));

        assertNotNull(a.id());
        assertNotEquals(a.id(), b.id());

    }

    @Test
    void equals() {
        ICard a = new MockedCard("mocked-card-a", List.of(BasicLands.SWAMP));
        assertEquals(a, new MockedCard(a.id(), a.name(), a.cost()));
    }
}
