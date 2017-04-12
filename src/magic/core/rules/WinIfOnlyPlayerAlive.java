package magic.core.rules;

import magic.core.states.State;

/**
 * @author ldavid
 */
public class WinIfOnlyPlayerAlive extends MagicRule {

    @Override
    public State beforePlayerAct(State state) {
        if (state.playerStates().stream().filter(State.PlayerState::isAlive).count() == 1) {
            // Only one player alive!
            return _finish(state);
        }

        return state;
    }
}
