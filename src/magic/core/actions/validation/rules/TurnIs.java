package magic.core.actions.validation.rules;

import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

/**
 * TurnIs Validation Rule.
 *
 * Check whether or not the current n-th turn in the state game
 * {@link State state} is {@link TurnIs#turn}.
 *
 * @author ldavid
 */
public class TurnIs extends ValidationRule {

    private final int turn;

    public TurnIs(int turn) {
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
