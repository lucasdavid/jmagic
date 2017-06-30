package org.games.jmagic.observers;

import org.games.jmagic.core.Game;
import org.games.jmagic.players.Player;
import org.games.jmagic.actions.Action;
import org.games.jmagic.actions.DisqualifyAction;
import org.games.jmagic.actions.FinishGameAction;
import org.games.jmagic.infrastructure.exceptions.ValidationException;
import org.games.jmagic.core.states.State;

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

    protected State _disqualify(State state) {
        return _disqualify(state, state.activePlayerState().player);
    }

    protected State _disqualify(State state, Player player) {
        return _apply_action(state, new DisqualifyAction(player));
    }

    protected State _finish(State state) {
        return _apply_action(state, new FinishGameAction());
    }

    private State _apply_action(State state, Action action) {
        try {
            return action
                .raiseForErrors(state)
                .update(state);
        } catch (ValidationException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
