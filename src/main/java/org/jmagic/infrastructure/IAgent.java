package org.jmagic.infrastructure;

import org.jmagic.actions.Action;
import org.jmagic.core.states.State;
import org.jmagic.players.Player;

/**
 * IAgent Interface.
 * <p>
 * Implementers of this interface should take a game state in
 * {@link IAgent#act(State)} and decide upon an action (an object
 * of the class {@link Action}) to return. This is most likely implemented by
 * classes such as players.
 * <p>
 * Take a look at {@link Player}
 *
 * @author ldavid
 */
public interface IAgent extends IIdentifiable {
    Action act(State state);
}
