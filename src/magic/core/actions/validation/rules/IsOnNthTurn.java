package magic.core.actions.validation.rules;

import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

public class IsOnNthTurn extends ValidationRule {

    private final int turn;

    public IsOnNthTurn(int turn) {
        this.turn = turn;
    }

    @Override
    public void onValidate(State state) {
        if (state.turn != turn) {
            errors.add(String.format("Incorrect turn (expected: %d, actual: %d)",
                turn, state.turn));
        }
    }
}
