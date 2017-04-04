package magic.core.contracts;

import magic.core.State;
import magic.core.actions.Action;

/**
 * Game Event Listener Interface.
 *
 * @author ldavid
 */
public interface IGameEventListener {
    Action act(State state);
}
