package org.jmagic.players;

import org.jmagic.actions.Action;
import org.jmagic.actions.AttachAction;
import org.jmagic.actions.DeclareAttackersAction;
import org.jmagic.actions.DeclareBlockersAction;
import org.jmagic.actions.PlayAction;
import org.jmagic.actions.validation.rules.players.active.HasLandsToPlayIt;
import org.jmagic.core.cards.ICard;
import org.jmagic.core.cards.Creature;
import org.jmagic.core.cards.lands.Land;
import org.jmagic.core.cards.attachments.Boost;
import org.jmagic.core.cards.attachments.IAttachable;
import org.jmagic.core.cards.attachments.IAttachment;
import org.jmagic.experts.IExpert;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.experts.MulliganExpert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NaivePlayer extends BasicPlayer {

    public NaivePlayer(String name) {
        this(name, new MulliganExpert());
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

    protected class CardsByTypeComparator implements Comparator<ICard> {

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
