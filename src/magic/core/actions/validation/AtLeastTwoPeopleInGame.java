package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;

public class AtLeastTwoPeopleInGame extends ValidationRule {

    @Override
    public void onValidate(State state, Player actor) {
        if (state.playerStates().size() < 2) {
            errors.add("passing requires at least two people");
        }
    }
}
