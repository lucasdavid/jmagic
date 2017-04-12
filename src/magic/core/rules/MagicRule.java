package magic.core.rules;

import magic.core.Game;
import magic.core.Player;
import magic.core.actions.Action;
import magic.core.actions.AdvanceGameAction;
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
public abstract class MagicRule {

    public static final Logger LOG = Logger.getLogger(Game.class.getName());

    public State beforePlayerAct(State state) {
        return state;
    }

    public State afterPlayerAct(State state, Action action) {
        return state;
    }

    protected State _disqualifyAndPass(State state) {
        return _disqualifyAndPass(state, state.activePlayerState().player);
    }

    protected State _disqualifyAndPass(State state, Player player) {
        try {
            state = new DisqualifyAction(player)
                .raiseForErrors(state)
                .update(state);
        } catch (JMagicException ex) {
            LOG.log(Level.WARNING, null, ex);
        } finally {
            state = _passOrFinish(state);
        }

        return state;
    }

    protected State _passOrFinish(State state) {
        try {
            return new AdvanceGameAction()
                .raiseForErrors(state)
                .update(state);
        } catch (JMagicException ex) {
            LOG.log(Level.WARNING, null, ex);
            return _finish(state);
        }
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
