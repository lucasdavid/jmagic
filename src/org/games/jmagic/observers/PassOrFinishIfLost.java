package org.games.jmagic.observers;

import org.games.jmagic.actions.Action;
import org.games.jmagic.actions.AdvanceGameAction;
import org.games.jmagic.infrastructure.exceptions.ValidationException;
import org.games.jmagic.core.states.State;

/**
 * PassOrFinishIfLost Observer.
 *
 * This observer will pass control onto another player if the current one isn't alive.
 *
 * @author ldavid
 */
public class PassOrFinishIfLost extends Observer {

    @Override
    public State afterPlayerAct(State state, Action action, long actStartedAt, long actEndedAt) {
        if (!state.activePlayerState().isAlive()) {
            try {
                return new AdvanceGameAction()
                    .raiseForErrors(state)
                    .update(state);
            } catch (ValidationException ex) {
                _finish(state);
            }
        }

        return state;
    }
}
