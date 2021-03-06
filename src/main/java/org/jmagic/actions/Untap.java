package org.jmagic.actions;

import org.jmagic.actions.rules.game.TurnStepIs;
import org.jmagic.actions.rules.players.IsTurnOfActivePlayer;
import org.jmagic.actions.rules.players.active.HasNotAlreadyUntappedInThisTurn;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.ITappable;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.jmagic.infrastructure.validation.rules.BasicRules.And;

/**
 * Untap Action.
 * <p>
 * Un-tap all tapped cards of player.
 *
 * @author ldavid
 */
public class Untap extends Action {

    private final Player player;

    public Untap(Player player) {
        this.player = player;
    }

    @Override
    public State update(State state) {
        List<State.PlayerState> players = state.playerStates().stream()
            .map(s -> s.isAlive() && s.player.equals(this.player)
                ? new State.PlayerState(s.player, s.life(), s.originalLife(),
                s.deck, s.hand,
                new Cards(s.field.cards().stream()
                    .map(c -> c instanceof ITappable ? ((ITappable) c).untap() : c)
                    .collect(Collectors.toList())),
                s.graveyard, s.attackers, s.blockers, s.playing)
                : s)
            .collect(Collectors.toList());
        return new State(players, state.turn, state.step, state.done,
            state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    public ValidationRule validationRules() {
        return And(
            new TurnStepIs(TurnSteps.UNTAP),
            new IsTurnOfActivePlayer(),
            new HasNotAlreadyUntappedInThisTurn());
    }

    @Override
    public boolean equals(Object o) {
        try {
            return this == o || player.equals(((Untap) o).player);
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 103;
        hash = 13 * hash + Objects.hashCode(player);
        return hash;
    }
}
