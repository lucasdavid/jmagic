package org.jmagic.experts;

import org.jmagic.players.Player;
import org.jmagic.core.states.State;

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
