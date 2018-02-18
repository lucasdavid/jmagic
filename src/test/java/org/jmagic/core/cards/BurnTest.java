package org.jmagic.core.cards;

import org.jmagic.core.cards.lands.BasicLands;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BurnTest {
    @Test
    void duplicate() {
        Burn burn = new Burn("test-burn-a", 2, List.of(BasicLands.FOREST));
        Burn copied = (Burn) burn.duplicate();

        assertNotEquals(burn, copied);
        assertNotEquals(burn.id(), copied.id());
        assertEquals(burn.name(), copied.name());
        assertEquals(burn.properties(), copied.properties());
        assertEquals(burn.effectiveProperties(), copied.effectiveProperties());
        assertEquals(burn.cost(), copied.cost());
        assertEquals(burn.damage(), copied.damage());
        assertEquals(burn.effectiveDamage(), copied.effectiveDamage());
        assertEquals(burn.maxTargetCount(), copied.maxTargetCount());
    }

    @Test
    void testToString() {
        Burn burn = new Burn("test-burn-a", 2, List.of(BasicLands.FOREST));
        assertEquals("\"test-burn-a\" d:2 (1 enemies)", burn.toString());
    }

    @Test
    void testToString1() {
        Burn burn = new Burn("test-burn-a", 2, List.of(BasicLands.FOREST));
        assertEquals("\"test-burn-a\" c:[FOREST] d:2 (1 enemies)", burn.toString(true));
    }

}
