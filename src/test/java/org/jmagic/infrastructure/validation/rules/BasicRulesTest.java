package org.jmagic.infrastructure.validation.rules;

import org.jmagic.core.states.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * BasicRulesTest.
 *
 * @author ldavid
 */
class BasicRulesTest {

    private ValidationRule t1;
    private ValidationRule t2;

    private class AlwaysTrueTestRule extends ValidationRule {
        @Override
        public void onValidate(State state) {
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }
    }

    @BeforeEach
    void setUp() {
        t1 = new AlwaysTrueTestRule();
        t2 = new AlwaysFalseTestRule();
    }

    private class AlwaysFalseTestRule extends AlwaysTrueTestRule {

        @Override
        public void onValidate(State state) {
            errors.add("failed");
        }
    }

    @Test
    void testSanity() {
        assertNotNull(BasicRules.And());
        assertNotNull(BasicRules.Or());
        assertNotNull(BasicRules.Not(BasicRules.IsTrue(state -> false)));
    }

    @Test
    void or() {
        ValidationRule expected = new Or(t1, t2);
        ValidationRule actual = BasicRules.Or(t1, t2);

        assertEquals(expected, actual);
    }

    @Test
    void and() {
        ValidationRule expected = new And(t1, t2);
        ValidationRule actual = BasicRules.And(t1, t2);

        assertEquals(expected, actual);
    }

    @Test
    void not() {
        ValidationRule expected = new Not(t1);
        ValidationRule actual = BasicRules.Not(t1);

        assertEquals(expected, actual);
    }

    @Test
    void isTrue() {
        Predicate<State> r = state -> true;
        ValidationRule expected = new IsTrue(r);
        ValidationRule actual = BasicRules.IsTrue(r);

        assertEquals(expected, actual);
    }
}
