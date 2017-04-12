package magic.core.actions.validation.rules.players;

import magic.core.Player;
import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

public class IsPlaying extends ValidationRule {

    private final Player player;

    public IsPlaying() {
        this(null);
    }

    public IsPlaying(Player player) {
        this.player = player;
    }

    @Override
    public void onValidate(State state) {
        State.PlayerState playerState = player != null
            ? state.playerState(player)
            : state.activePlayerState();

        if (!playerState.playing) {
            errors.add(String.format("%s isn't playing", player));
        }
    }
}
