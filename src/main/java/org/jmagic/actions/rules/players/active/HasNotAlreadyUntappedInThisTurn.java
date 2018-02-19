package org.jmagic.actions.rules.players.active;

import org.jmagic.actions.Action;
import org.jmagic.actions.Untap;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;


public class HasNotAlreadyUntappedInThisTurn extends ValidationRule {

    @Override
    public void onValidate(State state) {
        // Validate that Player isFrom drawing only once in a turn!
        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;
        State.PlayerState active = state.activePlayerState();

        while (previous != null && previous.turn == state.turn) {
            if (active.player.equals(previous.activePlayerState().player)
                    && actionExecutedInPrevious instanceof Untap) {
                errors.add(String.format("%s already untapped their cards this turn", active));
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }
    }
}
