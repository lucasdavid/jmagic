package org.jmagic.actions.validation.rules.players;

import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.Player;

import java.util.Arrays;
import java.util.Collection;


public class ArePlaying extends ValidationRule {

    private final Collection<Player> players;

    public ArePlaying(Player... players) {
        this(Arrays.asList(players));
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
