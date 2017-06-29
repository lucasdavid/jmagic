package magic.infrastructure.validation.basic;

import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ConnectivesTest.
 *
 * @author ldavid
 */
class ConnectivesTest {

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
    void or() {
        ValidationRule expected = new Or(t1, t2);
        ValidationRule actual = Connectives.Or(t1, t2);

        assertEquals(expected, actual);
    }

    @Test
    void and() {
        ValidationRule expected = new And(t1, t2);
        ValidationRule actual = Connectives.And(t1, t2);

        assertEquals(expected, actual);
    }

    @Test
    void not() {
        ValidationRule expected = new Not(t1);
        ValidationRule actual = Connectives.Not(t1);

        assertEquals(expected, actual);
    }

    @Test
    void isTrue() {
        Predicate<State> r = state -> true;
        ValidationRule expected = new IsTrue(r);
        ValidationRule actual = Connectives.IsTrue(r);

        assertEquals(expected, actual);
    }
}
