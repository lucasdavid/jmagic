package org.games.jmagic.actions;

import org.games.jmagic.actions.validation.rules.game.TurnsStepIs;
import org.games.jmagic.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import org.games.jmagic.actions.validation.rules.players.active.HasNotAlreadyUntappedInThisTurn;
import org.games.jmagic.core.cards.Cards;
import org.games.jmagic.core.cards.ITappable;
import org.games.jmagic.core.states.State;
import org.games.jmagic.core.states.TurnSteps;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;
import org.games.jmagic.players.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.games.jmagic.infrastructure.validation.basic.Connectives.And;

/**
 * Untap Action.
 * <p>
 * Un-tap all tapped cards of player.
 *
 * @author ldavid
 */
public class UntapAction extends Action {

    private final Player player;

    public UntapAction(Player player) {
        this.player = player;
    }

    @Override
    public State update(State state) {
        List<State.PlayerState> players = state.playerStates().stream()
            .map(s -> s.isAlive() && s.player.equals(this.player)
                ? new State.PlayerState(s.player, s.life(), s.maxLife(),
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
            new TurnsStepIs(TurnSteps.UNTAP),
            new ActiveAndTurnsPlayersAreTheSame(),
            new HasNotAlreadyUntappedInThisTurn());
    }

    @Override
    public boolean equals(Object o) {
        try {
            return this == o || player.equals(((UntapAction) o).player);
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
