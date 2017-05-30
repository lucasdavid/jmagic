package magic.infrastructure.validation.rules;

import magic.core.exceptions.ValidationException;
import magic.core.states.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * ValidationRuleTest.
 *
 * @author ldavid
 */
class ValidationRuleTest {
    @Test
    void validate() {
        ValidationRule rule;

        rule = new TestRule();
        assertTrue(rule.isValid(null));

        rule = new TestProblematicRule();
        assertFalse(rule.isValid(null));
    }

    @Test
    void raiseForErrors() {
        try {
            new TestRule().validate(null).raiseForErrors();
        } catch (ValidationException ex) {
            fail("shouldn't be raising any errors");
        }

        assertThrows(ValidationException.class,
            () -> new TestProblematicRule().validate(null).raiseForErrors());
        assertThrows(IllegalStateException.class,
            () -> new TestProblematicRule().raiseForErrors());
    }

    @Test
    void raiseForErrors1() {
        try {
            new TestRule().raiseForErrors(null);
        } catch (ValidationException ex) {
            fail("shouldn't be raising any errors");
        }

        assertThrows(ValidationException.class,
            () -> new TestProblematicRule().raiseForErrors(null));
    }

    @Test
    void isValid() {
        assertTrue(new TestRule().validate(null).isValid());
        assertFalse(new TestProblematicRule().validate(null).isValid());

        assertThrows(IllegalStateException.class, () -> new TestRule().isValid());
        assertThrows(IllegalStateException.class, () -> new TestProblematicRule().isValid());
    }

    @Test
    void isValid1() {
        assertTrue(new TestRule().isValid(null));
        assertFalse(new TestProblematicRule().isValid(null));
    }

    @Test
    void errors() {
        TestProblematicRule rule = new TestProblematicRule();
        assertEquals(rule.errors(), rule.errors);
    }

    private class TestRule extends ValidationRule {

        @Override
        public void onValidate(State state) {
        }
    }

    private class TestProblematicRule extends ValidationRule {

        @Override
        public void onValidate(State state) {
            errors.add("a generic error");
        }
    }

}