package org.jmagic.observers;

import org.jmagic.actions.Action;
import org.jmagic.actions.DisqualifyAction;
import org.jmagic.actions.FinishGameAction;
import org.jmagic.core.Game;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.exceptions.ValidationException;
import org.jmagic.players.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Magic Event Listener.
 * <p>
 * Listens to all events in a Magic game and doesn't do anything.
 *
 * @author ldavid
 */
public abstract class Observer {

    public static final Logger LOG = Logger.getLogger(Game.class.getName());

    public State beforePlayerAct(State state) {
        return state;
    }

    public State afterPlayerAct(State state, Action action, long actStartedAt, long actEndedAt) {
        return state;
    }

    protected State disqualify(State state) {
        return disqualify(state, state.activePlayerState().player);
    }

    protected State disqualify(State state, Player player) {
        return applyAction(state, new DisqualifyAction(player));
    }

    protected State finish(State state) {
        return applyAction(state, new FinishGameAction());
    }

    private State applyAction(State state, Action action) {
        try {
            return action
                .raiseForErrors(state)
                .update(state);
        } catch (ValidationException ex) {
            LOG.log(Level.SEVERE, this + " could not apply action " + action, ex);
            return null;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
