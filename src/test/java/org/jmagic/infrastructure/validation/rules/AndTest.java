package org.jmagic.infrastructure.validation.rules;

import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.And;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testEquals() {
        ValidationRule r1 = new And(new TestTrueRule(), new TestTrueRule()),
                       r2 = new And(new TestTrueRule(), new TestTrueRule()),
                       r3 = new And(new TestTrueRule(), new TestFalseRule());

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertNotEquals(r1, null);
    }

    @Test
    void testToString() {
        ValidationRule r = new And(new TestTrueRule(), new TestTrueRule(), new TestFalseRule());
        assertEquals("And[TestTrueRule, TestTrueRule, TestFalseRule]", r.toString());
    }

    private class TestTrueRule extends ValidationRule {

        @Override
        public void onValidate(State state) {
        }

        @Override
        public boolean equals(Object obj) {
            return getClass().equals(obj.getClass());
        }
    }

    private class TestFalseRule extends ValidationRule {

        @Override
        public void onValidate(State state) {
            errors.add("an error");
        }

        @Override
        public boolean equals(Object obj) {
            return getClass().equals(obj.getClass());
        }
    }

}
