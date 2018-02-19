package org.jmagic.experts;

import org.jmagic.actions.Action;
import org.jmagic.actions.InitialDraw;
import org.jmagic.core.states.State;
import org.jmagic.players.Player;

/**
 * Mulligan Expert.
 *
 * This class is expert in Mulligan. That is, re-drawing the initial hand
 * when the drawing player isn't satisfied with the one they've got.
 *
 * @author ldavid
 */
public class MulliganExpert implements IExpert {

    public int count(State state, Player player) {
        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;
        int initialDrawsCount = 0;
        while (previous != null && previous.step == state.step) {
            if (player.equals(previous.activePlayerState().player)
                && actionExecutedInPrevious instanceof InitialDraw) {
                initialDrawsCount++;
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }

        return initialDrawsCount;
    }
}
