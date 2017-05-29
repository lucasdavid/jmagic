package magic.core.actions;

import magic.core.Player;
import magic.core.actions.validation.rules.game.TurnsStepIs;
import magic.core.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import magic.core.actions.validation.rules.players.ArePlaying;
import magic.core.actions.validation.rules.players.HasPerformedThisTurn;
import magic.core.actions.validation.rules.players.active.HasCardsInField;
import magic.core.cards.ICard;
import magic.core.cards.creatures.Creature;
import magic.core.states.State;
import magic.core.states.TurnSteps;
import magic.infrastructure.validation.rules.ValidationRule;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static magic.infrastructure.validation.connectives.Connectives.And;
import static magic.infrastructure.validation.connectives.Connectives.Not;

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
            p.life(), p.maxLife(),
            p.deck, p.hand, p.field, p.graveyard,
            this.attackers, p.blockers, p.playing));

        return new State(ps, state.turn, state.step,
            state.done, state.turnsPlayerIndex, state.activePlayerIndex,
            this, state);
    }

    @Override
    public ValidationRule validationRules() {
        // TODO: prevent players from attacking themselves.
        return And(
            new TurnsStepIs(TurnSteps.DECLARE_ATTACKERS),
            new ActiveAndTurnsPlayersAreTheSame(),
            // All attackers are in player's field.
            new HasCardsInField(attackers.keySet()
                .stream()
                .map(c -> (ICard) c)
                .collect(Collectors.toSet())),
            new ArePlaying(attackers.values()));
    }

    @Override
    public String toString() {
        return String.format("%s %s", super.toString(), attackers);
    }
}
