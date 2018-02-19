package org.jmagic.observers;

import org.jmagic.actions.Action;
import org.jmagic.actions.AdvanceGame;
import org.jmagic.core.states.State;

/**
 * Patient Observer.
 * <p>
 * Adding this to a game's observer pool will result in the game being place
 * more slowly. Each action will now be separated by
 * {@link HumanObserver#seconds} seconds.
 *
 * @author ldavid
 */
public class HumanObserver extends Observer {
    private final double seconds;

    /**
     * @param seconds necessary for the human observer to understand an action.
     */
    public HumanObserver(double seconds) {
        this.seconds = seconds;
    }

    @Override
    public State afterPlayerAct(State state, Action action, long actStartedAt, long actEndedAt) {
        if (!(action instanceof AdvanceGame)) {
            try {
                Thread.sleep(Math.round(1000 * seconds));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return state;
    }
}
