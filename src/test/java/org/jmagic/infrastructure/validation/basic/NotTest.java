package org.jmagic.infrastructure.validation.basic;

import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NotTest.
 *
 * @author ldavid
 */
class NotTest {

    @Test
    void onValidate() {
        ValidationRule rule = new Not(new AlwaysClean());
        assertFalse(rule.isValid(null));

        rule = new Not(new AlwaysProblematic());
        assertTrue(rule.isValid(null));
    }

    class AlwaysClean extends ValidationRule {

        @Override
        public void onValidate(State state) {
        }
    }

    class AlwaysProblematic extends ValidationRule {

        @Override
        public void onValidate(State state) {
            errors.add("an error");
        }
    }

}