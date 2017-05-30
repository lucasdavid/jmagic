package magic.core.states;

import magic.core.actions.Action;

/**
 * Game Event Listener Interface.
 * <p>
 * Implementers of this interface should take a game state in
 * {@link IGameEventListener#act(State)} and decide upon an action (an object
 * of the class {@link Action}) to return. This is most likely implemented by
 * classes such as players.
 * <p>
 * Take a look at {@link magic.core.Player}
 *
 * @author ldavid
 */
public interface IGameEventListener {
    Action act(State state);
}
