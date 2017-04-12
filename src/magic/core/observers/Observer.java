package magic.core.observers;

import magic.core.Game;
import magic.core.Player;
import magic.core.actions.Action;
import magic.core.actions.DisqualifyAction;
import magic.core.actions.FinishGameAction;
import magic.core.exceptions.InvalidActionException;
import magic.core.exceptions.JMagicException;
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
        try {
            state = new DisqualifyAction(player)
                .raiseForErrors(state)
                .update(state);
        } catch (InvalidActionException ignored) {
        }

        return state;
    }

    protected State _finish(State state) {
        try {
            return new FinishGameAction()
                .raiseForErrors(state)
                .update(state);
        } catch (JMagicException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
