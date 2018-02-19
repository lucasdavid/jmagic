package org.jmagic.core.cards;

import org.jmagic.core.cards.attachments.Boost;
import org.jmagic.core.cards.lands.BasicLands;
import org.jmagic.core.cards.lands.Land;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CardsTest {

    private Cards c;

    @BeforeEach
    void setUp() {
        this.c = new Cards(new Land(BasicLands.SWAMP),
            new Boost("boost-test", 3, 4, Collections.EMPTY_LIST),
            new Creature("test-creature", 1, 2, List.of(BasicLands.SWAMP)));
    }

    @Test
    void cards() {
        List<ICard> cards = c.cards();
        assertTrue(cards instanceof ArrayList);
        assertEquals(3, cards.size());
    }

    @Test
    void isEmpty() {
        assertFalse(new Cards(new Land(BasicLands.SWAMP)).isEmpty());
        assertTrue(new Cards().isEmpty());
    }

    @Test
    void contains() {
        Land l = new Land(BasicLands.SWAMP);

        c = new Cards(new Land(BasicLands.SWAMP),
            new Boost("boost-test", 3, 4, Collections.EMPTY_LIST),
            new Creature("test-creature", 1, 2, List.of(BasicLands.SWAMP)),
            l);

        assertTrue(c.contains(l));
        assertFalse(c.contains(new Creature("unknown-creature", 2, 2, Collections.EMPTY_LIST)));
    }

    @Test
    void getValidated() {
        Land l = new Land(BasicLands.SWAMP);

        c = new Cards(new Land(BasicLands.SWAMP),
            new Boost("boost-test", 3, 4, Collections.EMPTY_LIST),
            new Creature("test-creature", 1, 2, List.of(BasicLands.SWAMP)),
            l);

        Land untrusted = new Land(l.id(), l.kind(), l.tapped());

        ICard validated = c.validated(untrusted);
        assertEquals(l, validated);

    }

    @Test
    void size() {
        assertEquals(3, c.size());
        assertEquals(0, new Cards().size());
    }

    @Test
    void update() {
        Creature creature = ((Creature) c.cards().get(2)).takeDamage(1);
        Cards updatedCards = c.update(creature);

        assertEquals(
            ((Creature) c.cards().get(2)).effectiveLife() - 1,
            ((Creature) updatedCards.cards().get(2)).effectiveLife());
    }

    @Test
    void add() {
        Cards updatedCards = c.add(new Land(BasicLands.PLAINS),
            new Burn("test-burn-a", 2, Collections.emptyList()));

        assertEquals(c.size() + 2, updatedCards.size());
        assertTrue(updatedCards.cards().get(3) instanceof Land);
        assertTrue(updatedCards.cards().get(4) instanceof Burn);
    }

    @Test
    void remove() {
        Cards updatedCards = c.remove(c.cards().get(0), c.cards().get(1));

        assertEquals(1, updatedCards.size());
        assertEquals(c.cards().get(2), updatedCards.cards().get(0));
    }

    @Test
    void testEquals() {
        Land l = new Land(BasicLands.SWAMP);
        Cards c1 = new Cards(l),
            c2 = new Cards(l),
            c3 = new Cards(l, new Boost("test-boost", 1, 2, Collections.emptyList()));

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c2, c3);
        assertNotEquals(c1, null);
        assertNotEquals(c1, l);
    }

    @Test
    void testHashCode() {
        List<ICard> source = List.of(new Land(BasicLands.PLAINS),
            new Burn("burn-test", 2, Collections.emptyList()),
            new Creature("creature-test", 2, 2, List.of(BasicLands.PLAINS)));

        Cards c2 = new Cards(source);
        Set<Cards> cardSet = new HashSet<>(Set.of(c, c2));

        assertEquals(2, cardSet.size());
        assertTrue(cardSet.contains(c));
        assertTrue(cardSet.contains(c2));
        assertTrue(cardSet.contains(new Cards(source)));
        assertFalse(cardSet.add(new Cards(source)));
    }

    @Test
    void testToString() {
        assertEquals("[\"swamp\", \"boost-test\" d+:3 l+: 4, \"test-creature\" d:1 l:2]", c.toString());
    }

    @Test
    void testToString1() {
        assertEquals("[\"swamp\", \"boost-test\" c:[] d+:3 l+: 4, \"test-creature\" c:[SWAMP] d:1 l:2]", c.toString(true));
    }

}
