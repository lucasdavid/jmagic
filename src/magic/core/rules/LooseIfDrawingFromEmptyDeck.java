package magic.core.rules;

import magic.core.actions.AdvanceGameAction;
import magic.core.actions.DisqualifyAction;
import magic.core.actions.FinishGameAction;
import magic.core.exceptions.JMagicException;
import magic.core.states.State;
import magic.core.states.TurnStep;

import java.util.logging.Level;

/**
 * @author ldavid
 */
public class LooseIfDrawingFromEmptyDeck extends MagicRule {
    @Override
    public State beforePlayerAct(State state) {
        State.PlayerState p = state.activePlayerState();

        if (state.step == TurnStep.DRAW && p.deck.isEmpty()
            && state.turnsPlayerIndex == state.activePlayerIndex) {

            LOG.warning(String.format(
                "%s lost because `LooseIfDrawingFromEmptyDeck` rule is active",
                p.player));

            try {
                state = new DisqualifyAction(p.player).raiseForErrors(state).update(state);
                state = new AdvanceGameAction().raiseForErrors(state).update(state);
            } catch (JMagicException ex) {
                LOG.log(Level.SEVERE, null, ex);
                state = new FinishGameAction().update(state);
            }
        }

        return state;
    }
}
