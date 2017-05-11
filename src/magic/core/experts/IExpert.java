package magic.core.experts;

import magic.core.Player;
import magic.core.states.State;

/**
 * IExpert Interface.
 * <p>
 * Defines an expert towards some game aspect (e.g. an expert in mulligan).
 * Note: look for its sub-classes.
 *
 * @author ldavid
 */
public interface IExpert {

    int count(State state, Player player);
}
