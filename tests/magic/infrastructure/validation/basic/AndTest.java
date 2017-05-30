package magic.infrastructure.validation.basic;

import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AndTest.
 *
 * @author ldavid
 */
class AndTest {
    @Test
    void onValidate() {
        ValidationRule rule;

        rule = new And(new TestTrueRule());
        assertTrue(rule.isValid(null));

        rule = new And(new TestTrueRule(), new TestTrueRule());
        assertTrue(rule.isValid(null));

        rule = new And(new TestTrueRule(), new TestTrueRule(), new TestFalseRule());
        assertFalse(rule.isValid(null));

        rule = new And(new TestFalseRule());
        assertFalse(rule.isValid(null));

        rule = new And(new TestFalseRule(), new TestFalseRule(), new TestFalseRule());
        assertFalse(rule.isValid(null));
    }

    private class TestTrueRule extends ValidationRule {

        @Override
        public void onValidate(State state) {
        }
    }

    private class TestFalseRule extends ValidationRule {

        @Override
        public void onValidate(State state) {
            errors.add("an error");
        }
    }
}