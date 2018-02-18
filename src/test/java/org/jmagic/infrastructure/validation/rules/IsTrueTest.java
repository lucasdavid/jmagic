package org.jmagic.infrastructure.validation.rules;

import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.infrastructure.validation.rules.IsTrue;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IsTrueTest.
 *
 * @author ldavid
 */
class IsTrueTest {

    private State initialState;
    private State endState;

    @BeforeEach
    void setUp() {
        List<State.PlayerState> ps = null;

        initialState = new State(ps, 0, TurnSteps.UPKEEP,
            false, 0, 0, null, null);
        endState = new State(ps, 8, TurnSteps.END_OF_COMBAT,
            true, 0, 0, null, null);
    }

    @Test
    void onValidate() {
        ValidationRule rule = new IsTrue(state -> state.turn == 0);

        assertTrue(rule.isValid(initialState));
        assertFalse(rule.isValid(endState));
    }

    @Test
    void testEquals() {
        Predicate<State> p = state -> state.turn == 0;
        Predicate<State> p2 = state -> state.turn > 0;

        ValidationRule r1 = new IsTrue(p),
            r2 = new IsTrue(p),
            r3 = new IsTrue(p2);

        assertEquals(r1, r2);
        assertNotEquals(r1, null);
        assertNotEquals(r1, r3);
        assertNotEquals(r2, r3);
    }

    @Test
    void testToString() {
        IsTrue rule = new IsTrue(state -> state.done);

        assertEquals(rule.toString(), "IsTrue(Predicate)");
    }

}
