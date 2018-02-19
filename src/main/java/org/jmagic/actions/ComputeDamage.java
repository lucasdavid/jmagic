package org.jmagic.actions;

import org.jmagic.actions.rules.game.TurnStepIs;
import org.jmagic.actions.rules.players.IsTurnOfActivePlayer;
import org.jmagic.actions.rules.players.HasPerformedThisTurn;
import org.jmagic.core.cards.Creature;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.Player;

import java.util.*;
import java.util.stream.Collectors;

import static org.jmagic.infrastructure.validation.rules.BasicRules.And;
import static org.jmagic.infrastructure.validation.rules.BasicRules.Not;

/**
 * Compute Damage Action.
 * <p>
 * Player declares the damage suffered by each creature in their combat stage.
 *
 * @author ldavid
 */
public final class ComputeDamage extends Action {

    private Creature attacker;
    private List<Creature> blockers;

    @Override
    public State update(State state) {
        List<State.PlayerState> ps = state.playerStates();
        State.PlayerState p = state.activePlayerState();

        for (Map.Entry<Creature, Player> e : p.attackers.entrySet()) {
            attacker = (Creature) p.field.validated(e.getKey());
            if (!attacker.isAlive()) continue;

            State.PlayerState t = ps.stream()
                .filter(s -> s.player.equals(e.getValue()))
                .findFirst()
                .get();

            blockers = t.blockers.values().stream()
                .filter(a -> a.equals(this.attacker))
                .map(a -> (Creature) t.field.validated(a))
                .collect(Collectors.toList());

            if (blockers.isEmpty()) {
                // Damages target player's health.
                ps.set(ps.indexOf(t), t.takeDamage(attacker.effectiveDamage()));
                continue;
            }

            Combat c = new Combat().execute();

            ps.set(ps.indexOf(p), new State.PlayerState(p.player, p.life(), p.originalLife(),
                p.deck, p.hand,
                attacker.isAlive() ? p.field.update(attacker) : p.field.remove(attacker),
                attacker.isAlive() ? p.graveyard : p.graveyard.add(attacker),
                p.attackers, p.blockers, p.playing));

            ps.set(ps.indexOf(t), new State.PlayerState(t.player, t.life(), t.originalLife(),
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
            new TurnStepIs(TurnSteps.COMBAT_DAMAGE),
            new IsTurnOfActivePlayer(),
            Not(new HasPerformedThisTurn(ComputeDamage.class)));
    }

    @Override
    public boolean equals(Object o) {
        try {
            ComputeDamage c = (ComputeDamage) o;
            return this == c || attacker.equals(c.attacker) && blockers.equals(c.blockers);
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 19;
        hash = 3 * hash + Objects.hashCode(attacker);
        hash = 2 * hash + Objects.hashCode(blockers);
        return hash;
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
