package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;
import magic.core.actions.Action;
import magic.core.actions.DrawAction;

public class HasNotAlreadyDrawnInThisTurn extends ValidationRule {

    @Override
    public void onValidate(State state, Player actor) {
        // Validate that Player is drawing only once in a turn!
        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;

        while (previous != null && previous.turn == state.turn) {
            if (actor.equals(previous.currentPlayerState().player)
                    && actionExecutedInPrevious instanceof DrawAction) {
                errors.add(String.format("%s already drew this turn", actor));
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }
    }
}
