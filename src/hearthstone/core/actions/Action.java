package hearthstone.core.actions;

import hearthstone.core.State;
import hearthstone.core.exceptions.HearthStoneException;

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

}
