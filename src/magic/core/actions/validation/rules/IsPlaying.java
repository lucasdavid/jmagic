package magic.core.actions.validation.rules;

import magic.core.Player;
import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

public class IsPlaying extends ValidationRule {

    private final Player player;

    public IsPlaying(Player player) {
        this.player = player;
    }

    @Override
    public void onValidate(State state) {
        State.PlayerState playerState = state.playerState(player);
        if (!playerState.playing) {
            errors.add(String.format("{%s} isn't playing", player));
        }
    }
}
