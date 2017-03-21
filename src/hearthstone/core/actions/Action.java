package hearthstone.core.actions;

import hearthstone.core.State;
import hearthstone.core.exceptions.HearthStoneException;
import java.util.Objects;

/**
 *
 * @author ldavid
 */
public abstract class Action {

    public abstract State update(State state) throws HearthStoneException;

    public abstract void validActionOrRaisesException(State state) throws HearthStoneException;

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
