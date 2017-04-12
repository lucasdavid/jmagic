package magic.core.observers;

import magic.core.actions.Action;
import magic.core.states.State;

import java.util.Collection;

/**
 * @author ldavid
 */
public class LooseOnIllegalActionAttempt extends Observer {

    private final Collection<Class<? extends Action>> allowedActions;

    public LooseOnIllegalActionAttempt(Collection<Class<? extends Action>> allowedActions) {
        this.allowedActions = allowedActions;
    }

    @Override
    public State afterPlayerAct(State state, Action action, long actStartedAt, long actEndedAt) {
        if (!this.allowedActions.contains(action.getClass())) {
            State.PlayerState p = state.activePlayerState();

            LOG.warning(String.format("%s lost because %s is not listed as a legal action",
                p.player, action));

            return _disqualify(state);
        }

        return state;
    }
}
