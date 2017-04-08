package magic.core.cards;

import magic.core.IDamageable;
import magic.core.ITargetable;
import magic.core.cards.lands.BasicLands;
import magic.core.exceptions.IllegalCardUsageException;
import magic.core.exceptions.JMagicException;
import magic.core.states.State;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class Harmful extends Card {

    private final int damage;
    private final int maxTargetCount;

    public Harmful(UUID id, String name, int damage, Collection<BasicLands> cost) {
        this(id, name, damage, 1, cost);
    }

    public Harmful(UUID id, String name, int damage, int maxTargetCount, Collection<BasicLands> cost) {
        super(id, name, cost);
        this.damage = damage;
        this.maxTargetCount = maxTargetCount;
    }

    @Override
    public State use(State state, List<ITargetable> targets) {
        List<State.PlayerState> playerStates = state.playerStates();

        targets.forEach(t -> {
            if (t instanceof State.PlayerState) {
                // Update playerStates with their recomputed lives.
                int indexOfPlayer = playerStates.indexOf(t);
                playerStates.add(indexOfPlayer,
                    (State.PlayerState) playerStates
                        .remove(indexOfPlayer)
                        .takeDamage(effectiveDamage()));

            } else if (t instanceof Card) {
                // Find the player to whom that card belongs.
                State.PlayerState p = playerStates.stream()
                    .filter(_p -> _p.field.contains((Card) t))
                    .findFirst().get();

                // Update the card with its recomputed life.
                List<ICard> fieldCards = p.field.cards();
                int indexOfCard = fieldCards.indexOf(t);
                fieldCards.add(indexOfCard,
                    (Card) ((IDamageable) fieldCards
                        .remove(indexOfCard))
                        .takeDamage(effectiveDamage()));

                // Update player's info with the updated cards.
                int indexOfPlayer = playerStates.indexOf(p);
                playerStates.remove(indexOfPlayer);
                playerStates.add(indexOfPlayer,
                    new State.PlayerState(p.player, p.life(), p.maxLife(),
                        p.deck, p.hand, new Cards(fieldCards), p.graveyard,
                        p.playing));
            }
        });

        return new State(playerStates, state.turn, state.step, state.done, state.turnsPlayerIndex,
            state.activePlayerIndex, null, null);
    }

    @Override
    public void raiseForErrors(State state, List<ITargetable> targets)
        throws JMagicException {
        if (targets.isEmpty()) {
            throw new IllegalCardUsageException("must target at least one enemy");
        }

        if (!targets.stream().allMatch((t) -> t instanceof IDamageable)) {
            throw new IllegalCardUsageException("can only target damageable targets");
        }

        if (targets.stream()
            .map(t -> (IDamageable) t)
            .anyMatch(p -> !p.isAlive())) {
            throw new IllegalCardUsageException("cannot target a card that's not alive");
        }

        if (targets.stream()
            .filter(t -> t instanceof Card)
            .map(t -> (Card) t)
            .anyMatch(c ->
                state.playerStates().stream().noneMatch(i ->
                    i.field.contains(c)))) {
            throw new IllegalCardUsageException("can only target cards that are on the field");
        }

        if (targets.size() > maxTargetCount) {
            throw new IllegalCardUsageException(String.format("can only target %d entities", maxTargetCount));
        }
    }

    public int damage() {
        return damage;
    }

    public int effectiveDamage() {
        return damage();
    }

    public int maxTargetCount() {
        return maxTargetCount;
    }
}
