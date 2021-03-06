package org.jmagic.actions.rules.game;

import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.infrastructure.validation.rules.ValidationRule;


public class TurnStepIs extends ValidationRule {

    private final TurnSteps step;

    public TurnStepIs(TurnSteps step) {
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
