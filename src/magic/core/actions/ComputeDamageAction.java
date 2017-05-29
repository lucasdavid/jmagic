package magic.core.actions;

import magic.core.Player;
import magic.core.actions.validation.rules.game.TurnsStepIs;
import magic.core.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import magic.core.actions.validation.rules.players.HasPerformedThisTurn;
import magic.core.cards.creatures.Creature;
import magic.core.states.State;
import magic.core.states.TurnSteps;
import magic.infrastructure.validation.rules.ValidationRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static magic.infrastructure.validation.connectives.Connectives.And;
import static magic.infrastructure.validation.connectives.Connectives.Not;

/**
 * Compute Damage Action.
 * <p>
 * Player declares the damage suffered by each creature in their combat stage.
 *
 * @author ldavid
 */
public final class ComputeDamageAction extends Action {

    private Creature attacker;
    private List<Creature> blockers;

    @Override
    public State update(State state) {
        List<State.PlayerState> ps = state.playerStates();
        State.PlayerState p = state.activePlayerState();

        for (Map.Entry<Creature, Player> e : p.attackers.entrySet()) {
            attacker = (Creature) p.field.getValidated(e.getKey());
            if (!attacker.isAlive()) continue;

            State.PlayerState t = ps.stream()
                .filter(s -> s.player.equals(e.getValue()))
                .findFirst()
                .get();

            blockers = t.blockers.values().stream()
                .filter(a -> a.equals(this.attacker))
                .map(a -> (Creature) t.field.getValidated(a))
                .collect(Collectors.toList());

            if (blockers.isEmpty()) {
                // Damages target player's health.
                ps.set(ps.indexOf(t), t.takeDamage(attacker.effectiveDamage()));
                continue;
            }

            Combat c = new Combat().execute();

            ps.set(ps.indexOf(p), new State.PlayerState(p.player, p.life(), p.maxLife(),
                p.deck, p.hand,
                attacker.isAlive() ? p.field.update(attacker) : p.field.remove(attacker),
                attacker.isAlive() ? p.graveyard : p.graveyard.add(attacker),
                p.attackers, p.blockers, p.playing));

            ps.set(ps.indexOf(t), new State.PlayerState(t.player, t.life(), t.maxLife(),
                t.deck, t.hand,
                t.field
                    .update(new ArrayList<>(blockers))
                    .remove(new ArrayList<>(c.deadBlockers)),
                t.graveyard.add(new ArrayList<>(c.deadBlockers)),
                t.attackers, t.blockers, t.playing));
        }

        return new State(ps, state.turn, state.step,
            state.done, state.turnsPlayerIndex, state.activePlayerIndex,
            this, state);
    }

    @Override
    public ValidationRule validationRules() {
        return And(
            new TurnsStepIs(TurnSteps.COMBAT_DAMAGE),
            new ActiveAndTurnsPlayersAreTheSame(),
            Not(new HasPerformedThisTurn(ComputeDamageAction.class)));
    }

    private class Combat {

        Collection<Creature> deadBlockers = new HashSet<>();

        public Combat execute() {
            // Creatures swap damage.
            int i = 0;
            while (attacker.isAlive() && i < blockers.size()) {
                CombatRound r = new CombatRound(blockers.get(i));
                r.execute();
                blockers.set(i, r.blocker);
                i++;
            }

            deadBlockers.addAll(blockers.stream()
                .filter(b -> !b.isAlive())
                .collect(Collectors.toSet()));

            return this;
        }

        private class CombatRound {

            private Creature blocker;

            CombatRound(Creature blocker) {
                this.blocker = blocker;
            }

            public CombatRound execute() {
                blocker = blocker.takeDamage(attacker);
                attacker = attacker.takeDamage(blocker);
                return this;
            }
        }
    }
}
