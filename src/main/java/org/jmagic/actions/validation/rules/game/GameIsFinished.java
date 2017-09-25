package org.jmagic.actions.validation.rules.game;

import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;


public class GameIsFinished extends ValidationRule {

    @Override
    public void onValidate(State state) {
        if (!state.done) {
            errors.add("game is not finished");
        }
    }
}
