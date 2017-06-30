package org.games.jmagic.actions.validation.rules.players;

import org.games.jmagic.players.Player;
import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;

import java.util.Collection;
import java.util.Set;


public class ArePlaying extends ValidationRule {

    private final Collection<Player> players;

    public ArePlaying(Player player) {
        this(Set.of(player));
    }

    public ArePlaying(Collection<Player> players) {
        this.players = players;
    }

    @Override
    public void onValidate(State state) {
        for (Player player : players) {
            State.PlayerState p = player == null
                ? state.activePlayerState()
                : state.playerState(player);

            if (!p.playing) {
                errors.add(String.format("%s isn't playing", player));
            }
        }
    }
}