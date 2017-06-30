package org.games.jmagic.actions.validation.rules.game;

import org.games.jmagic.core.states.State;
import org.games.jmagic.core.states.TurnSteps;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;


public class TurnsStepIs extends ValidationRule {

    private final TurnSteps step;

    public TurnsStepIs(TurnSteps step) {
        this.step = step;
    }

    @Override
    public void onValidate(State state) {
        if (state.step != this.step) {
            errors.add(String.format("Incorrect turn's step (expected: %s, actual: %s)",
                this.step.name(), state.step.name()));
        }
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", super.toString(), step);
    }
}
