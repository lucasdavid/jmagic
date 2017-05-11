package magic.core.actions.validation.rules.game;

import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;


public class GameIsFinished extends ValidationRule {

    @Override
    public void onValidate(State state) {
        if (!state.done) {
            errors.add("game is not finished");
        }
    }
}
