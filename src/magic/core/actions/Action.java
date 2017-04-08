package magic.core.actions;

import magic.core.actions.validation.ValidationRule;
import magic.core.exceptions.InvalidActionException;
import magic.core.exceptions.ValidationException;
import magic.core.states.State;

import java.util.Collection;
import java.util.Objects;

/**
 * Action Base Class.
 * <p>
 * Basic interface for a {@link magic.core.Player}'s action.
 *
 * @author ldavid
 */
public abstract class Action {

    public abstract State update(State state);

    /**
     * Checks if the application of this action will bring the game to an invalid
     * state and, in this case, throws an exception.
     * <p>
     * This contract should always be implemented in the sub-classes of {@link Action},
     * as it dependents on the domain and logic of each and every specific action.
     *
     * @param state the state in which this action will be applied.
     * @throws InvalidActionException raised when the application of this
     *                                action results in an invalid state for the game.
     */
    public Action raiseForErrors(State state) throws InvalidActionException {
        for (ValidationRule r : validationRules()) {
            try {
                r.validate(state).raiseForErrors();
            } catch (ValidationException e) {
                throw new InvalidActionException(e.getMessage());
            }
        }

        return this;
    }

    protected abstract Collection<ValidationRule> validationRules();

    /**
     * Check whether or not the application of this action will result in a valid state.
     *
     * @param state the state that will have its integrity verified.
     * @return true, if the action's application results in a valid state. False, otherwise.
     */
    public boolean isValid(State state) {
        return validationRules().stream()
                .allMatch(r -> r.validate(state).isValid());
    }

    @Override
    public boolean equals(Object o) {
        return o != null && getClass().equals(o.getClass());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash * Objects.hashCode(getClass());
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
