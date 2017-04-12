package magic.core.rules;

import magic.core.actions.Action;
import magic.core.states.State;

/**
 * @author ldavid
 */
public class LooseIfNullAction extends MagicRule {

    @Override
    public State afterPlayerAct(State state, Action action) {
        if (action == null) {
            State.PlayerState p = state.activePlayerState();

            LOG.warning(String.format(
                "%s lost because `LooseIfNullAction` rule is active " +
                "(try `AdvanceGameAction` if your intention is to perform no actions)",
                p.player));

            return _disqualifyAndPass(state);
        }

        return state;
    }
}
