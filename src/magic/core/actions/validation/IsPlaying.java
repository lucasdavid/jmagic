package magic.core.actions.validation;

import magic.core.State;

public class IsPlaying extends ValidationRule {

    private final int playerId;
    private final String message;

    public IsPlaying(int playerId) {
        this(playerId, String.format("player {%s} is not playing", playerId));
    }

    public IsPlaying(int playerId, String message) {
        this.playerId = playerId;
        this.message = message;
    }

    @Override
    public void onValidate(State state) {
        if (!state.playerInfo(playerId).playing) {
            errors.add(message);
        }
    }
}
