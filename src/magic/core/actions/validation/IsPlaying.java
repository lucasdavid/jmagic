package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;

public class IsPlaying extends ValidationRule {

    private final Player player;

    public IsPlaying(Player player) {
        this.player = player;
    }

    @Override
    public void onValidate(State state) {
        if (!state.playerState(player).playing) {
            errors.add(String.format("{%s} isn't playing", player));
        }
    }
}
