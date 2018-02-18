package org.jmagic.core.cards;

import org.jmagic.core.cards.attachments.Boost;
import org.jmagic.core.cards.lands.BasicLands;
import org.jmagic.core.cards.lands.Land;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

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

        ICard validated = c.getValidated(untrusted);
        assertEquals(l, validated);

    }

    @Test
    void size() {
        assertEquals(3, c.size());
        assertEquals(0, new Cards().size());
    }

    @Test
    void update() {
    }

    @Test
    void update1() {
    }

    @Test
    void add() {
    }

    @Test
    void add1() {
    }

    @Test
    void remove() {
    }

    @Test
    void remove1() {
    }

    @Test
    void testEquals() {
        Land l = new Land(BasicLands.SWAMP);
        Cards c1 = new Cards(l),
              c2 = new Cards(l),
              c3 = new Cards(l, new Boost("test-boost", 1, 2, Collections.emptyList()));

        assertTrue(c1.equals(c2));
        assertFalse(c1.equals(c3));
        assertFalse(c2.equals(c3));
        assertFalse(c1.equals(null));
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
    }

    @Test
    void testToString1() {
    }

}