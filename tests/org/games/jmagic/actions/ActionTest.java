package org.games.jmagic.actions;

import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.basic.IsTrue;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author ldavid
 */
public class ActionTest {

    /**
     * Test of equals method, of class Action.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");

        Action i;

        i = new DrawAction();

        assertFalse(i.equals(null));
        assertTrue(i.equals(new DrawAction()));
        assertFalse(i.equals(new AdvanceGameAction()));

        i = new AdvanceGameAction();

        assertFalse(i.equals(null));
        assertFalse(i.equals(new DrawAction()));
        assertTrue(i.equals(new AdvanceGameAction()));
    }

    private class TestAction extends Action {

        @Override
        public State update(State state) {
            return new State(state.playerStates(), state.turn + 1, state.step,
                state.done, state.turnsPlayerIndex,
                state.activePlayerIndex, state.actionThatLedToThisState, state);
        }

        @Override
        public ValidationRule validationRules() {
            return new IsTrue(state -> state.turn < 50);
        }
    }
}