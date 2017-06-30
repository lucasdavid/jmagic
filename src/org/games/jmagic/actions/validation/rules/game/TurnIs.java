package org.games.jmagic.actions.validation.rules.game;

import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;

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

    @Override
    public String toString() {
        return String.format("%s(%d)", super.toString(), turn);
    }
}
