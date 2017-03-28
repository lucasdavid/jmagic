package hearthstone.core.cards;

import hearthstone.core.IDamageable;
import hearthstone.core.ITargetable;
import hearthstone.core.State;
import hearthstone.core.exceptions.HearthStoneException;
import hearthstone.core.exceptions.IllegalCardUsageException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class Harmful extends Card {
    private final int damage;

    public Harmful(String name, int damage, int manaCost) {
        this(UUID.randomUUID(), name, damage, manaCost);
    }

    public Harmful(UUID id, String name, int damage, int manaCost) {
        super(id, name, manaCost);
        this.damage = damage;
    }

    @Override
    public State use(State state, List<ITargetable> targets) {
        // Update damages.
        targets = targets.stream()
                .map(t -> ((IDamageable) t).takeDamage(getDamage()))
                .collect(Collectors.toList());

        // Update playersInfo with their recomputed lives.
        List<State.PlayerInfo> playersInfoWithUpdatedPlayersLife = state.getPlayersInfo();

        targets.stream().filter(t -> t instanceof State.PlayerInfo)
                .map(t -> (State.PlayerInfo) t)
                .forEach(i -> {
                    playersInfoWithUpdatedPlayersLife.remove(i);
                    playersInfoWithUpdatedPlayersLife.add(i);
                });

        List<Card> updatedCards = targets.stream()
                .filter(t -> t instanceof Card)
                .map(t -> (Card) t)
                .collect(Collectors.toList());

        List<State.PlayerInfo> playersInfoWithUpdatedCards = playersInfoWithUpdatedPlayersLife.stream()
                .map(p -> new State.PlayerInfo(p.player, p.life, p.deck, p.hand,
                        p.field.removeAll(updatedCards).addAll(updatedCards),
                        p.graveyard))
                .collect(Collectors.toList());

        return new State(playersInfoWithUpdatedCards,
                state.turn, state.done, state.turnsCurrentPlayerId);
    }

    @Override
    public void validUseOrRaisesException(State state, List<ITargetable> targets)
            throws HearthStoneException {
        if (targets.isEmpty()) {
            throw new IllegalCardUsageException(this + " must target at least one enemy");
        }

        if (!targets.stream().allMatch((t) -> t instanceof IDamageable)) {
            throw new IllegalCardUsageException("can only target damageable targets");
        }

        if (targets.stream()
                .filter(t -> t instanceof Card)
                .map(t -> (Card) t)
                .anyMatch(c -> state.getPlayersInfo().stream().noneMatch(i -> i.field.contains(c)))) {
            throw new IllegalCardUsageException("can only target cards that are on the field");
        }
    }

    public int getDamage() {
        return damage;
    }
}
