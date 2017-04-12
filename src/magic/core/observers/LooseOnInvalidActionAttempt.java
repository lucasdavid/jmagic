package magic.core.observers;

import magic.core.actions.Action;
import magic.core.exceptions.InvalidActionException;
import magic.core.states.State;

/**
 * @author ldavid
 */
public class LooseOnInvalidActionAttempt extends Observer {

    @Override
    public State afterPlayerAct(State state, Action action, long actStartedAt, long actEndedAt) {
        try {
            action.raiseForErrors(state);
        } catch (InvalidActionException ex) {
            State.PlayerState p = state.activePlayerState();
            LOG.warning(String.format("%s lost because they are attempting an invalid action: %s (details: %s)",
                p.player, action, ex.getMessage()));

            return _disqualify(state);
        }

        return state;
    }
}
