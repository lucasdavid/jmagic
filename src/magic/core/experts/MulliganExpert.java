package magic.core.experts;

import magic.core.Player;
import magic.core.actions.Action;
import magic.core.actions.InitialDrawAction;
import magic.core.states.State;
import magic.core.states.TurnStep;

/**
 * Initial Draw IExpert.
 *
 * @author ldavid
 */
public class MulliganExpert implements IExpert {

    public int count(State state, Player player) {
        assert state.step == TurnStep.DRAW;

        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;
        int initialDrawsCount = 0;
        while (previous != null && previous.step == state.step) {
            if (player.equals(previous.activePlayerState().player)
                && actionExecutedInPrevious instanceof InitialDrawAction) {
                initialDrawsCount++;
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }

        return initialDrawsCount;
    }
}
