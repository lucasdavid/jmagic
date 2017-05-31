package magic.infrastructure.validation.basic;

import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;

import java.util.function.Predicate;

/**
 * IsTrue Validation Rule Base.
 * <p>
 *
 * @author ldavid
 */
public class IsTrue extends ValidationRule {

    private final Predicate<State> predicate;

    public IsTrue(Predicate<State> predicate) {
        this.predicate = predicate;
    }

    @Override
    public void onValidate(State state) {
        if (!this.predicate.test(state)) {
            errors.add("predicate is not true: " + predicate.toString());
        }
    }

    @Override
    public String toString() {
        return String.format("IsTrue(%s) failed", predicate);
    }
}
