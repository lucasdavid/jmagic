package org.jmagic.actions.validation.rules.players.active;

import org.jmagic.actions.Action;
import org.jmagic.actions.DrawAction;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;


public class HasNotAlreadyDrawnInThisTurn extends ValidationRule {

    @Override
    public void onValidate(State state) {
        // Validate that Player isFrom drawing only once in a turn!
        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;
        State.PlayerState p = state.activePlayerState();

        while (previous != null && previous.turn == state.turn) {
            if (p.player.equals(previous.turnsPlayerState().player)
                && actionExecutedInPrevious instanceof DrawAction) {
                errors.add(String.format("%s already drew this turn", p));
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }
    }
}
