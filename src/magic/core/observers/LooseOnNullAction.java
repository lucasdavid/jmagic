package magic.core.observers;

import magic.core.actions.Action;
import magic.core.states.State;

/**
 * LooseOnNullAction Observer.
 * <p>
 * Adding this to a game's observer pool will result in a player loosing when
 * attempting a {@code null} action.
 *
 * @author ldavid
 */
public class LooseOnNullAction extends Observer {

    @Override
    public State afterPlayerAct(State state, Action action, long actStartedAt, long actEndedAt) {
        if (action == null) {
            State.PlayerState p = state.activePlayerState();

            LOG.warning(String.format(
                "%s lost because `LooseOnNullAction` rule is active " +
                    "(try `AdvanceGameAction` if your intention is to perform no actions)",
                p.player));

            return _disqualify(state);
        }

        return state;
    }
}
