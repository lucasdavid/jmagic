package org.jmagic.infrastructure.validation.rules;

import org.jmagic.core.states.State;

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
            errors.add("predicate is not true: " + predicate.getClass().getSimpleName());
        }
    }

    @Override
    public boolean equals(Object o) {
        try {
            return this == o || this.predicate.equals(((IsTrue) o).predicate);
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%s(Predicate)",
            getClass().getSimpleName());
    }
}
