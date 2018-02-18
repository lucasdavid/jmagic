package org.jmagic.core.cards.lands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LandTest {
    private Land land, tappedLand;

    @BeforeEach
    void setUp() {
        land = new Land(BasicLands.PLAINS);
        tappedLand = new Land(BasicLands.MOUNTAIN, true);
    }

    @Test
    void kind() {
        assertEquals(BasicLands.PLAINS, land.kind());
        assertEquals(BasicLands.MOUNTAIN, tappedLand.kind());
    }

    @Test
    void tap() {
        assertFalse(land.tapped());
        assertTrue(land.tap().tapped());

        assertTrue(tappedLand.tapped());
        assertTrue(tappedLand.tap().tapped());
    }

    @Test
    void untap() {
        assertFalse(land.tapped());
        assertFalse(land.untap().tapped());

        assertTrue(tappedLand.tapped());
        assertFalse(tappedLand.untap().tapped());
    }

    @Test
    void tapped() {
        assertFalse(land.tapped());
        assertTrue(tappedLand.tapped());
    }

    @Test
    void duplicate() {
        Land copy = (Land) land.duplicate();

        assertNotEquals(land.id(), copy.id());
        assertEquals(land.kind(), copy.kind());
        assertEquals(land.tapped(), copy.tapped());
        assertEquals(land.properties(), copy.properties());
        assertEquals(land.name(), copy.name());
    }

    @Test
    void testToString() {
        assertEquals("\"plains\"", land.toString());
        assertEquals("\"mountain\"", tappedLand.toString());
    }

    @Test
    void testToString1() {
        assertEquals("\"plains\"", land.toString(true));
        assertEquals("\"mountain\"", tappedLand.toString(true));
    }

}
