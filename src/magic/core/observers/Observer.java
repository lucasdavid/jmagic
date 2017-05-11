package magic.core.observers;

import magic.core.Game;
import magic.core.Player;
import magic.core.actions.Action;
import magic.core.actions.AdvanceGameAction;
import magic.core.actions.DisqualifyAction;
import magic.core.actions.FinishGameAction;
import magic.core.exceptions.ValidationException;
import magic.core.states.State;

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
