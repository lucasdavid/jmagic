package magic.core.actions.validation.rules;

import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

public class GameIsFinished extends ValidationRule {

    @Override
    public void onValidate(State state) {
        if (!state.done) {
            errors.add("game is not finished");
        }
    }
}
