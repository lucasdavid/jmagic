package magic.core.actions;

import magic.core.actions.validation.rules.game.TurnsStepIs;
import magic.core.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import magic.core.actions.validation.rules.players.CardsAreInAnyField;
import magic.core.actions.validation.rules.players.active.HasCardsInField;
import magic.core.cards.ICard;
import magic.core.cards.creatures.Creature;
import magic.core.states.State;
import magic.core.states.TurnSteps;
import magic.infrastructure.validation.rules.ValidationRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static magic.infrastructure.validation.connectives.Connectives.And;
import static magic.infrastructure.validation.connectives.Connectives.Not;

/**
 * Declare Defenders Action.
 * <p>
 * Targeted player declares which creatures will block the incoming attack
 * this turn.
 *
 * @author ldavid
 */
public final class DeclareBlockersAction extends Action {

    private final Map<Creature, Creature> blockers;

    public DeclareBlockersAction(Map<Creature, Creature> blockers) {
        this.blockers = blockers;
    }

    @Override
    public State update(State state) {
        List<State.PlayerState> ps = state.playerStates();
        State.PlayerState p = state.activePlayerState();

        ps.set(ps.indexOf(p), new State.PlayerState(p.player,
            p.life(), p.maxLife(),
            p.deck, p.hand, p.field, p.graveyard,
            p.attackers, this.blockers, p.playing));

        return new State(state.playerStates(), state.turn, state.step,
            state.done, state.turnsPlayerIndex, state.activePlayerIndex,
            this, state);
    }

    @Override
    public ValidationRule validationRules() {
        return And(
            new TurnsStepIs(TurnSteps.DECLARE_BLOCKERS),
            // Because the turn's player only declares attackers.
            Not(new ActiveAndTurnsPlayersAreTheSame()),
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
    public String toString() {
        return String.format("%s %s", super.toString(), blockers);
    }
}
