package org.games.jmagic.actions.validation.rules.players;

import org.games.jmagic.players.Player;
import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;


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
