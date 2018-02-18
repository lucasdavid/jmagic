package org.jmagic.core.states;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurnStepsTest {
    @Test
    void phase() {
        assertEquals(TurnSteps.Phase.COMBAT, TurnSteps.DECLARE_ATTACKERS.phase());
        assertEquals(TurnSteps.Phase.BEGINNING, TurnSteps.DRAW.phase());
        assertEquals(TurnSteps.Phase.PRE_COMBAT, TurnSteps.MAIN_1.phase());
        assertEquals(TurnSteps.Phase.POST_COMBAT, TurnSteps.MAIN_2.phase());
    }

    @Test
    void in() {
        assertTrue(TurnSteps.DRAW.in(TurnSteps.Phase.BEGINNING));
        assertTrue(TurnSteps.DECLARE_ATTACKERS.in(TurnSteps.Phase.COMBAT));
        assertTrue(TurnSteps.DECLARE_BLOCKERS.in(TurnSteps.Phase.COMBAT));
        assertTrue(TurnSteps.MAIN_1.in(TurnSteps.Phase.PRE_COMBAT));
        assertTrue(TurnSteps.MAIN_2.in(TurnSteps.Phase.POST_COMBAT));
    }

    @Test
    void next() {
        assertEquals(TurnSteps.UNTAP, TurnSteps.INITIAL_DRAWING.next());
        assertEquals(TurnSteps.DRAW, TurnSteps.UPKEEP.next());
        assertEquals(TurnSteps.DECLARE_BLOCKERS, TurnSteps.DECLARE_ATTACKERS.next());
    }

    @Test
    void isFirst() {
        assertTrue(TurnSteps.UNTAP.isFirst());
        assertFalse(TurnSteps.DRAW.isFirst());
    }

    @Test
    void isLast() {
        assertTrue(TurnSteps.CLEANUP.isLast());
        assertFalse(TurnSteps.COMBAT_DAMAGE.isLast());
    }

}