package org.jmagic.actions;

import org.jmagic.actions.validation.rules.game.TurnsStepIs;
import org.jmagic.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import org.jmagic.actions.validation.rules.players.ArePlaying;
import org.jmagic.actions.validation.rules.players.PlayerIs;
import org.jmagic.actions.validation.rules.players.active.HasCardsInField;
import org.jmagic.core.cards.Creature;
import org.jmagic.core.cards.ICard;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.Player;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.jmagic.infrastructure.validation.basic.Connectives.*;

/**
 * Declare Attackers Action.
 * <p>
 * Player declares which creatures will be attacking this turn.
 *
 * @author ldavid
 */
public final class DeclareAttackersAction extends Action {

    private final Map<Creature, Player> attackers;

    public DeclareAttackersAction(Map<Creature, Player> attackers) {
        this.attackers = attackers;
    }

    @Override
    public State update(State state) {
        List<State.PlayerState> ps = state.playerStates();
        State.PlayerState p = state.activePlayerState();

        ps.set(ps.indexOf(p), new State.PlayerState(p.player,
            p.life(), p.originalLife(),
            p.deck, p.hand, p.field, p.graveyard,
            this.attackers, p.blockers, p.playing));

        return new State(ps, state.turn, state.step,
            state.done, state.turnsPlayerIndex, state.activePlayerIndex,
            this, state);
    }

    @Override
    public ValidationRule validationRules() {
        return And(
            new TurnsStepIs(TurnSteps.DECLARE_ATTACKERS),
            new ActiveAndTurnsPlayersAreTheSame(),
            // All attackers are in player's field.
            new HasCardsInField(attackers.keySet()
                .stream()
                .map(c -> (ICard) c)
                .collect(Collectors.toSet())),
            new ArePlaying(attackers.values()),
            Or(
                IsTrue(state -> attackers.isEmpty()),
                Not(new PlayerIs(attackers.values()))
            )
        );
    }

    @Override
    public boolean equals(Object o) {
        try {
            DeclareAttackersAction c = (DeclareAttackersAction) o;
            return this == c || attackers.equals(c.attackers);
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 31;
        hash = 3 * hash + Objects.hashCode(attackers);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%s %s", super.toString(), attackers);
    }
}
