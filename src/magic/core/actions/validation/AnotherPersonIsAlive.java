package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;

public class AnotherPersonIsAlive extends ValidationRule {

    @Override
    public void onValidate(State state, Player actor) {
        if (state.playerStates().stream().noneMatch(s -> s.player != actor && s.isAlive())) {
            errors.add("passing requires that a person different than the current one to be playing");
        }
    }
}
