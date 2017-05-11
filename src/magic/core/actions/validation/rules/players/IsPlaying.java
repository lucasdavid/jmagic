package magic.core.actions.validation.rules.players;

import magic.core.Player;
import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;


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
        State.PlayerState playerState = player == null
            ? state.activePlayerState()
            : state.playerState(player);

        if (!playerState.playing) {
            errors.add(String.format("%s isn't playing", player));
        }
    }
}
