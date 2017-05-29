package magic.players;

import magic.core.Player;
import magic.core.actions.Action;
import magic.core.actions.DeclareAttackersAction;
import magic.core.actions.DeclareBlockersAction;
import magic.core.actions.PlayAction;
import magic.core.actions.validation.rules.players.active.HasLandsToPlayIt;
import magic.core.cards.creatures.Creature;
import magic.core.cards.lands.Land;
import magic.core.experts.IExpert;
import magic.core.states.State;
import magic.core.states.TurnSteps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NaivePlayer extends BasicPlayer {

    public NaivePlayer(IExpert mulliganExpert) {
        super(mulliganExpert);
    }

    public NaivePlayer(String name, IExpert mulliganExpert) {
        super(name, mulliganExpert);
    }

    @Override
    public Action act(State state) {
        State.PlayerState myState = state.playerState(this);

        if (state.step == TurnSteps.MAIN_2) {
            try {
                return new PlayAction(myState.hand.cards().stream()
                    .filter(c -> !(c instanceof Land) && new HasLandsToPlayIt(this, c).isValid(state))
                    .findAny()
                    .get());
            } catch (NoSuchElementException ignored) {
            }
        }

        if (state.step == TurnSteps.DECLARE_ATTACKERS
            && state.activePlayerIndex == state.turnsPlayerIndex
            && !(state.actionThatLedToThisState instanceof DeclareAttackersAction)) {

            Map<Creature, Player> attackers = new HashMap<>();
            List<Player> enemies = state.playerStates().stream()
                .filter(p -> !this.equals(p.player))
                .map(s -> s.player)
                .collect(Collectors.toList());

            myState.field.cards().stream()
                .filter(c -> c instanceof Creature && !((Creature) c).tapped())
                .map(c -> (Creature) c)
                .forEach(attacker -> attackers.put(attacker, enemies.get(0)));

            return new DeclareAttackersAction(attackers);
        }

        if (state.step == TurnSteps.DECLARE_BLOCKERS
            && !(state.actionThatLedToThisState instanceof DeclareBlockersAction)) {
            List<Creature> attackers = state
                .turnsPlayerState()
                    .attackers.entrySet().stream()
                    .filter(e -> this.equals(e.getValue()))
                    .map(Map.Entry::getKey)
                    .sorted(Comparator.comparingInt(Creature::effectiveDamage))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (!attackers.isEmpty()) {
                // There are creature attacking me, declare blockers...
                Map<Creature, Creature> blockers = new HashMap<>();

                myState.field.cards().stream()
                    .filter(c -> c instanceof Creature && !((Creature) c).tapped())
                    .map(c -> (Creature) c)
                    .sorted(Comparator.comparingInt(Creature::effectiveLife))
                    .forEach(blocker -> {
                        if (!attackers.isEmpty()) {
                            Creature attacker = attackers.remove(0);
                            blockers.put(blocker, attacker);
                        }
                    });

                return new DeclareBlockersAction(blockers);
            }
        }

        // Don't know what to do. Ask for the superclass.
        return super.act(state);
    }
}
