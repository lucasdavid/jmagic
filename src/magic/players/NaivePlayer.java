package magic.players;

import magic.core.Player;
import magic.core.actions.Action;
import magic.core.actions.AttachAction;
import magic.core.actions.DeclareAttackersAction;
import magic.core.actions.DeclareBlockersAction;
import magic.core.actions.PlayAction;
import magic.core.actions.validation.rules.players.active.HasLandsToPlayIt;
import magic.core.cards.ICard;
import magic.core.cards.creatures.Creature;
import magic.core.cards.lands.Land;
import magic.core.cards.magics.attachments.Boost;
import magic.core.cards.magics.attachments.IAttachable;
import magic.core.cards.magics.attachments.IAttachment;
import magic.core.experts.IExpert;
import magic.core.states.State;
import magic.core.states.TurnSteps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            Optional<ICard> any = myState.hand.cards().stream()
                .filter(c -> !(c instanceof Land)
                    && new HasLandsToPlayIt(this, c).isValid(state))
                .sorted(new CardsByTypeComparator())
                .findAny();

            if (any.isPresent()) {
                boolean hasAttachablesInField = myState.field.cards().stream()
                    .anyMatch(c -> c instanceof IAttachable);

                if (!(any.get() instanceof IAttachment) || hasAttachablesInField) {
                    return new PlayAction(any.get());
                }
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

        // Checks if any detached attachments onto my field.
        // If there are, then I must attach them to another card.
        Optional<IAttachment> detached = myState.field.cards().stream()
            .filter(c -> c instanceof IAttachment)
            .map(c -> (IAttachment) c)
            .findAny();

        if (detached.isPresent()) {
            Stream<ICard> targets;
            IAttachment card = detached.get();

            if (card instanceof Boost) {
                // Always targets myself when boosting.
                targets = myState.field.cards().stream();
            } else {
                // It's probably a curse, let's place it onto someone else.
                targets = state.playerStates().stream()
                    .filter(p -> !p.equals(myState))
                    .flatMap(p -> p.field.cards().stream());
            }

            IAttachable target = targets
                .filter(c -> c instanceof IAttachable)
                .map(c -> (IAttachable) c)
                .findAny()
                .get();

            return new AttachAction(detached.get(), List.of(target));
        }

        // Don't know what to do. Ask for the superclass.
        return super.act(state);
    }

    private class CardsByTypeComparator implements Comparator<ICard> {

        /**
         * order for each one of the cards. This allows creatures and burns to
         * come first and attachments afterwards.
         */
        private Map<String, Integer> order = Map.of(
            "Creature", 1,
            "Burn", 2,
            "Boost", 3,
            "IAttachment", 4);

        @Override
        public int compare(ICard o1, ICard o2) {
            return order.get(o1.getClass().getSimpleName()).compareTo(
                order.get(o2.getClass().getSimpleName()));
        }
    }
}
