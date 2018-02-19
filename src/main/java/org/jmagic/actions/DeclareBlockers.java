package org.jmagic.actions;

import org.jmagic.actions.rules.game.TurnStepIs;
import org.jmagic.actions.rules.players.IsTurnOfActivePlayer;
import org.jmagic.actions.rules.cards.CardsAreInAnyField;
import org.jmagic.actions.rules.players.active.HasCardsInField;
import org.jmagic.core.cards.Creature;
import org.jmagic.core.cards.ICard;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.infrastructure.validation.rules.ValidationRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.jmagic.infrastructure.validation.rules.BasicRules.And;
import static org.jmagic.infrastructure.validation.rules.BasicRules.Not;

/**
 * Declare Defenders Action.
 * <p>
 * Targeted player declares which creatures will block the incoming attack
 * this turn.
 *
 * @author ldavid
 */
public final class DeclareBlockers extends Action {

    private final Map<Creature, Creature> blockers;

    public DeclareBlockers(Map<Creature, Creature> blockers) {
        this.blockers = blockers;
    }

    @Override
    public State update(State state) {
        List<State.PlayerState> ps = state.playerStates();
        State.PlayerState p = state.activePlayerState();

        ps.set(ps.indexOf(p), new State.PlayerState(p.player,
                p.life(), p.originalLife(),
                p.deck, p.hand, p.field, p.graveyard,
                p.attackers, this.blockers, p.playing));

        return new State(state.playerStates(), state.turn, state.step,
                state.done, state.turnsPlayerIndex, state.activePlayerIndex,
                this, state);
    }

    @Override
    public ValidationRule validationRules() {
        return And(
                new TurnStepIs(TurnSteps.DECLARE_BLOCKERS),
                // Because the turn's player only declares attackers.
                Not(new IsTurnOfActivePlayer()),
                // All blockers are in this player's field.
                new HasCardsInField(blockers.keySet()
                        .stream()
                        .map(c -> (ICard) c)
                        .collect(Collectors.toSet())),
                // All attackers are in some field.
                new CardsAreInAnyField(
                        new ArrayList<>(blockers.values())));
    }

    @Override
    public boolean equals(Object o) {
        try {
            DeclareBlockers c = (DeclareBlockers) o;
            return this == c || blockers.equals(c.blockers);
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 37;
        hash = 2 * hash + Objects.hashCode(blockers);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%s %s", super.toString(), blockers);
    }
}
