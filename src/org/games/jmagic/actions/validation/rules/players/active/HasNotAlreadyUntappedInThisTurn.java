package org.games.jmagic.actions.validation.rules.players.active;

import org.games.jmagic.actions.Action;
import org.games.jmagic.actions.UntapAction;
import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;


public class HasNotAlreadyUntappedInThisTurn extends ValidationRule {

    @Override
    public void onValidate(State state) {
        // Validate that Player isFrom drawing only once in a turn!
        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;
        State.PlayerState active = state.activePlayerState();

        while (previous != null && previous.turn == state.turn) {
            if (active.player.equals(previous.turnsPlayerState().player)
                    && actionExecutedInPrevious instanceof UntapAction) {
                errors.add(String.format("%s already untapped their cards this turn", active));
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }
    }
}
