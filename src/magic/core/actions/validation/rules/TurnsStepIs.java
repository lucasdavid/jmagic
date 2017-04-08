package magic.core.actions.validation.rules;

import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;
import magic.core.states.TurnStep;

public class TurnsStepIs extends ValidationRule {

    private final TurnStep step;

    public TurnsStepIs(TurnStep step) {
        this.step = step;
    }

    @Override
    public void onValidate(State state) {
        if (state.step != this.step) {
            errors.add(String.format("Incorrect turn's step (expected: %s, actual: %s)",
                this.step.name(), state.step.name()));
        }
    }
}
