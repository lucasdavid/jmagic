package org.jmagic.observers;

import org.jmagic.actions.Action;
import org.jmagic.infrastructure.exceptions.ValidationException;
import org.jmagic.core.states.State;

/**
 * LooseOnInvalidActionAttempt Observer.
 * <p>
 * Adding this to a game's observer pool will result in a player loosing if
 * they attempt an action that's not valid to the current state of the game.
 *
 * @author ldavid
 */
public class LooseOnInvalidActionAttempt extends Observer {

    @Override
    public State afterPlayerAct(State state, Action action, long actStartedAt, long actEndedAt) {
        try {
            action.raiseForErrors(state);
        } catch (ValidationException ex) {
            LOG.warning(String.format("%s lost because they are attempting an invalid action: %s (details: %s)",
                state.activePlayerState().player, action, ex.getMessage()));

            return _disqualify(state);
        }

        return state;
    }
}
