package org.jmagic.observers;

import org.jmagic.actions.Action;
import org.jmagic.core.states.State;

/**
 * FinishIfLastPlayersAlive Observer.
 * <p>
 * This observer checks if there's currently {@link FinishIfLastPlayersAlive#maxWinners}
 * alive player in the game. If that's the case, it ends the game.
 *
 * @author ldavid
 */
public class FinishIfLastPlayersAlive extends Observer {

    private final int maxWinners;

    public FinishIfLastPlayersAlive() {
        this(1);
    }

    public FinishIfLastPlayersAlive(int maxWinners) {
        this.maxWinners = maxWinners;
    }

    @Override
    public State beforePlayerAct(State state) {
        if (state.playerStates().stream().filter(State.PlayerState::isAlive).count() <= maxWinners) {
            return finish(state);
        }

        return state;
    }

    @Override
    public State afterPlayerAct(State state, Action action, long actStartedAt, long actEndedAt) {
        return beforePlayerAct(state);
    }
}
