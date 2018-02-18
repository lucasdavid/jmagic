package org.jmagic.core.cards;

import org.jmagic.core.cards.lands.BasicLands;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class CardTest {
    @Test
    void id() {
        ICard c = new MockedCard("test-card", List.of(BasicLands.ISLAND));
        assertNotNull(c.id());

        UUID expectedId = UUID.randomUUID();
        c = new MockedCard(expectedId, "test-card", List.of(BasicLands.ISLAND));
        assertEquals(expectedId, c.id());
    }

    @Test
    void name() {
        ICard c = new MockedCard("test-card-c", List.of(BasicLands.ISLAND));
        assertEquals("test-card-c", c.name());
    }

    @Test
    void properties() {
        ICard c = new MockedCard(UUID.randomUUID(), "test-card-c",
            List.of(Properties.HASTE, Properties.PROVOKE), List.of(BasicLands.ISLAND));

        assertEquals(List.of(Properties.HASTE, Properties.PROVOKE), c.properties());
    }

    @Test
    void properties1() {
        ICard c = new MockedCard("test-card-c", List.of(BasicLands.ISLAND));
        assertTrue(c.properties().isEmpty());
    }

    @Test
    void effectiveProperties() {
        ICard c = new MockedCard(UUID.randomUUID(), "test-card-c",
            List.of(Properties.HASTE, Properties.PROVOKE), List.of(BasicLands.ISLAND));

        assertEquals(List.of(Properties.HASTE, Properties.PROVOKE), c.effectiveProperties());
    }

    @Test
    void effectiveProperties1() {
        ICard c = new MockedCard("test-card-c", List.of(BasicLands.ISLAND));
        assertTrue(c.effectiveProperties().isEmpty());
    }

    @Test
    void cost() {
        ICard c = new MockedCard("test-card-c", List.of(BasicLands.ISLAND));
        assertEquals(List.of(BasicLands.ISLAND), c.cost());
    }

    @Test
    void cost1() {
        ICard c = new MockedCard("test-card-c", List.of());
        assertTrue(c.cost().isEmpty());
    }

    @Test
    void cost2() {
        ICard c = new MockedCard("test-card-c", List.of(BasicLands.ISLAND, BasicLands.SWAMP));
        assertEquals(List.of(BasicLands.ISLAND, BasicLands.SWAMP), c.cost());
    }

    @Test
    void testHashCode() {
        ICard c1 = new MockedCard("test-card-a", List.of(BasicLands.ISLAND, BasicLands.SWAMP)),
            c2 = new MockedCard("test-card-b", List.of(BasicLands.ISLAND)),
            c3 = new MockedCard("test-card-c", Collections.emptyList());

        Set<ICard> cards = new HashSet<>(Set.of(c1, c2));

        assertTrue(cards.contains(c1));
        assertTrue(cards.contains(c2));
        assertFalse(cards.contains(c3));
        assertFalse(cards.add(c1));
        assertFalse(cards.add(c2));
    }

    @Test
    void testToString() {
    }

    @Test
    void toString1() {
    }

    @Test
    void getId() {
        ICard a = new MockedCard("mocked-card-a", List.of(BasicLands.PLAINS));
        ICard b = new MockedCard("mocked-card-b", List.of(BasicLands.ISLAND));

        assertNotNull(a.id());
        assertNotEquals(a.id(), b.id());

    }

    @Test
    void testEquals() {
        ICard a = new MockedCard("mocked-card-a", List.of(BasicLands.SWAMP));
        assertEquals(a, new MockedCard(a.id(), a.name(), a.cost()));
        assertNotEquals(a, null);
        assertNotEquals(a, "InvalidCard");
    }

    private class MockedCard extends Card {

        MockedCard(String name, Collection<BasicLands> cost) {
            this(UUID.randomUUID(), name, cost);
        }

        MockedCard(UUID id, String name, Collection<BasicLands> cost) {
            this(id, name, Collections.emptyList(), cost);
        }

        MockedCard(UUID id, String name, Collection<Properties> properties, Collection<BasicLands> cost) {
            super(id, name, properties, cost);
        }

        @Override
        public ICard duplicate() {
            return new MockedCard(name(), cost());
        }
    }

}
