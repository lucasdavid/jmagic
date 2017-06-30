package org.games.jmagic.actions.validation.rules.game;

import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;


public class GameIsFinished extends ValidationRule {

    @Override
    public void onValidate(State state) {
        if (!state.done) {
            errors.add("game is not finished");
        }
    }
}
