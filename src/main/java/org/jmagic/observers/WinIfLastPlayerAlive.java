package org.jmagic.observers;

import org.jmagic.actions.Action;
import org.jmagic.core.states.State;

/**
 * WinIfLastPlayerAlive Observer.
 * <p>
 * This observer checks if there's currently
 * {@link WinIfLastPlayerAlive#maxWinners} alive player in the game. If that's
 * the case, it ends the game.
 *
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
