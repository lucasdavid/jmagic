package magic.core.observers;

import magic.core.actions.Action;
import magic.core.states.State;

/**
 * @author ldavid
 */
public class WinIfLastPlayerAlive extends Observer {

    private final int maxWinners;

    public WinIfLastPlayerAlive() {
        this(1);
    }

    public WinIfLastPlayerAlive(int maxWinners) {
        this.maxWinners = maxWinners;
    }

    @Override
    public State beforePlayerAct(State state) {
        if (state.playerStates().stream().filter(State.PlayerState::isAlive).count() <= maxWinners) {
            return _finish(state);
        }

        return state;
    }

    @Override
    public State afterPlayerAct(State state, Action action, long actStartedAt, long actEndedAt) {
        return beforePlayerAct(state);
    }
}
