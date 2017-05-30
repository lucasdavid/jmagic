package magic.infrastructure.validation.basic;

import magic.core.states.State;
import magic.core.states.TurnSteps;
import magic.infrastructure.validation.rules.ValidationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

}