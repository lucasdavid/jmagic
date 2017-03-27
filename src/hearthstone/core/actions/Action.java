package hearthstone.core.actions;

import hearthstone.core.State;
import hearthstone.core.exceptions.HearthStoneException;

import java.util.Objects;

/**
 * Action Base Class.
 * <p>
 * Basic interface for a {@link hearthstone.core.Player}'s action.
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
     * @throws HearthStoneException raised when the application of this
     *                              action results in an invalid state for the game.
     */
    public abstract void validActionOrRaisesException(State state) throws HearthStoneException;

    /**
     * Check whether or not the application of this action will result in a valid state.
     *
     * @param state the state that will have its integrity verified.
     * @return true, if the action's application results in a valid state. False, otherwise.
     */
    public boolean isValid(State state) {
        try {
            validActionOrRaisesException(state);
            return true;
        } catch (HearthStoneException ex) {
            return false;
        }
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
}
