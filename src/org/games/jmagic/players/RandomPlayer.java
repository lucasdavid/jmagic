package org.games.jmagic.players;

import org.games.jmagic.actions.Action;
import org.games.jmagic.actions.AttachAction;
import org.games.jmagic.actions.DeclareAttackersAction;
import org.games.jmagic.actions.DeclareBlockersAction;
import org.games.jmagic.core.cards.Creature;
import org.games.jmagic.core.cards.ICard;
import org.games.jmagic.core.cards.attachments.Boost;
import org.games.jmagic.core.cards.attachments.IAttachable;
import org.games.jmagic.core.cards.attachments.IAttachment;
import org.games.jmagic.core.states.State;
import org.games.jmagic.core.states.TurnSteps;
import org.games.jmagic.experts.IExpert;
import org.games.jmagic.experts.MulliganExpert;
import org.games.jmagic.infrastructure.collectors.CustomCollectors;

import java.util.*;
import java.util.stream.Stream;

/**
 * RandomPlayer.
 *
 * @author ldavid
 */
public class RandomPlayer extends NaivePlayer {

    private final Random random;

    public RandomPlayer() {
        this(new MulliganExpert());
    }

    public RandomPlayer(Random random) {
        this(new MulliganExpert(), random);
    }

    public RandomPlayer(String name) {
        this(name, new MulliganExpert(), new Random());
    }

    public RandomPlayer(IExpert mulliganExpert) {
        this(mulliganExpert, new Random());
    }

    public RandomPlayer(IExpert mulliganExpert, Random random) {
        super(mulliganExpert);
        this.random = random;
    }

    public RandomPlayer(String name, IExpert mulliganExpert, Random random) {
        super(name, mulliganExpert);
        this.random = random;
    }

    @Override
    public Action act(State state) {
        State.PlayerState myState = state.playerState(this);

        if (state.step == TurnSteps.DECLARE_ATTACKERS
                && state.activePlayerIndex == state.turnsPlayerIndex
                && !(state.actionThatLedToThisState instanceof DeclareAttackersAction)) {

            Map<Creature, Player> attackers = new HashMap<>();
            List<Player> enemies = state.playerStates().stream()
                    .filter(p -> !this.equals(p.player))
                    .map(s -> s.player)
                    .collect(CustomCollectors.toShuffledList(random));

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
                    .collect(CustomCollectors.toShuffledList(random));

            if (!attackers.isEmpty()) {
                // There are creature attacking me, declare blockers...
                Map<Creature, Creature> blockers = new HashMap<>();

                myState.field.cards().stream()
                        .filter(c -> c instanceof Creature && !((Creature) c).tapped())
                        .map(c -> (Creature) c)
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
                    .collect(CustomCollectors.toShuffledList(random))
                    .get(0);

            return new AttachAction(detached.get(), List.of(target));
        }

        // Don't know what to do. Ask for the superclass.
        return super.act(state);
    }
}
