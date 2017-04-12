package magic.core.actions.validation.rules.players;

import magic.core.Player;
import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

public class PlayersOtherThanActiveAreAlive extends ValidationRule {

    private final long minAlivePlayersCount;

    public PlayersOtherThanActiveAreAlive() {
        this(1);
    }

    public PlayersOtherThanActiveAreAlive(long minAlivePlayersCount) {
        this.minAlivePlayersCount = minAlivePlayersCount;
    }

    @Override
    public void onValidate(State state) {
        Player active = state.activePlayerState().player;
        long actualAlivePlayersCount = state.playerStates().stream()
            .filter(s -> !s.player.equals(active) && s.isAlive())
            .count();

        if (actualAlivePlayersCount < minAlivePlayersCount) {
            errors.add(String.format(
                "not enough players are alive (expected: %d, actual: %d)",
                minAlivePlayersCount, actualAlivePlayersCount));
        }
    }
}
