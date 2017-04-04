package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;

public class ActorIsCurrentPlayer extends ValidationRule {

    @Override
    public void onValidate(State state, Player actor) {
        if (!actor.equals(state.currentPlayerState().player)) {
            errors.add(String.format("%s is not the current player {%s}",
                    actor, state.currentPlayerState().player));
        }
    }
}
