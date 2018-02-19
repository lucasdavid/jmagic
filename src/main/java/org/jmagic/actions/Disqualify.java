package org.jmagic.actions;

import org.jmagic.actions.rules.players.ArePlaying;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.Player;

import java.util.List;
import java.util.Objects;

/**
 * Disqualify a player from the game.
 *
 * @author ldavid
 */
public class Disqualify extends Action {

    private final Player player;

    public Disqualify(Player player) {
        this.player = player;
    }

    @Override
    public State update(State state) {
        List<State.PlayerState> players = state.playerStates();
        State.PlayerState p = state.playerState(player);

        players.set(
            players.indexOf(p),
            new State.PlayerState(p.player, p.life(), p.originalLife(), p.deck,
                p.hand, p.field, p.graveyard, p.attackers, p.blockers, false));

        return new State(players, state.turn, state.step, state.done,
            state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    public boolean equals(Object o) {
        try {
            return this == o || player.equals(((Disqualify) o).player);
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 87;
        hash = 13 * hash + Objects.hashCode(player);
        return hash;
    }

    @Override
    public ValidationRule validationRules() {
        return new ArePlaying(player);
    }
}
