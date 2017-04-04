package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;

public class IsPlaying extends ValidationRule {

    @Override
    public void onValidate(State state, Player actor) {
        if (!state.playerState(actor).playing) {
            errors.add(String.format("{%s} isn't playing", actor));
        }
    }
}
