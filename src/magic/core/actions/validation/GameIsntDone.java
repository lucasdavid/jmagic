package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;

public class GameIsntDone extends ValidationRule {

    @Override
    public void onValidate(State state, Player actor) {
        if (state.done) {
            errors.add("cannot finish a game that's already done");
        }
    }
}
