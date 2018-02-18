package org.jmagic.observers;

import org.jmagic.actions.AdvanceGameAction;
import org.jmagic.infrastructure.exceptions.ValidationException;
import org.jmagic.core.states.State;

/**
 * PassOrFinishIfLost Observer.
 *
 * This observer will pass control onto another player if the current one isn't alive.
 *
 * @author ldavid
 */
public class PassOrFinishIfLost extends Observer {

    @Override
    public State beforePlayerAct(State state) {
        if (!state.activePlayerState().isAlive()) {
            try {
                return new AdvanceGameAction()
                    .raiseForErrors(state)
                    .update(state);
            } catch (ValidationException ex) {
                return finish(state);
            }
        }

        return state;
    }
}
