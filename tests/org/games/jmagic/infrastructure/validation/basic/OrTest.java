package org.games.jmagic.infrastructure.validation.basic;

import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OrTest.
 *
 * @author ldavid
 */
class OrTest {
    @Test
    void onValidate() {
        ValidationRule rule;

        rule = new Or(new TestTrueRule(), new TestTrueRule());
        assertTrue(rule.isValid(null));

        rule = new Or(new TestTrueRule(), new TestTrueRule(), new TestFalseRule());
        assertTrue(rule.isValid(null));

        rule = new Or(new TestFalseRule());
        assertFalse(rule.isValid(null));

        rule = new Or(new TestFalseRule(), new TestFalseRule(), new TestFalseRule());
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