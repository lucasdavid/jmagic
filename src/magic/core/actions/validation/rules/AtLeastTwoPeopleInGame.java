package magic.core.actions.validation.rules;

import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

public class AtLeastTwoPeopleInGame extends ValidationRule {

    @Override
    public void onValidate(State state) {
        if (state.playerStates().size() < 2) {
            errors.add("passing requires at least two people");
        }
    }
}
