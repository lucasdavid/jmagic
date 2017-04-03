package magic.core.actions.validation;

import magic.core.State;
import magic.core.actions.Action;
import magic.core.actions.DrawAction;

public class HasNotAlreadyDrawnInThisTurn extends ValidationRule {

    private final String message;

    public HasNotAlreadyDrawnInThisTurn() {
        this("cannot draw more than once in a turn");
    }

    public HasNotAlreadyDrawnInThisTurn(String message) {
        this.message = message;
    }

    @Override
    public void onValidate(State state) {
        // Validate that Player is drawing only once in a turn!
        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;

        while (previous != null && previous.turn == state.turn) {
            if (previous.turnsCurrentPlayerIndex == state.turnsCurrentPlayerIndex
                    && actionExecutedInPrevious instanceof DrawAction) {
                errors.add(message);
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }
    }
}
