package magic.core.cards.magics.attachments;

import magic.core.State;
import magic.core.cards.Card;
import magic.core.contracts.ICard;
import magic.core.contracts.ITargetable;
import magic.core.contracts.attachments.IAttachable;
import magic.core.contracts.attachments.IDamageBoost;
import magic.core.contracts.attachments.ILifeBoost;
import magic.core.exceptions.MagicException;
import magic.core.exceptions.IllegalCardUsageException;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class DamageLifeBoost extends Card implements IDamageBoost, ILifeBoost {

    public static Collection<DamageLifeBoost> DEFAULT_CARDS = List.of(
            new DamageLifeBoost("Sword of Dawn", 2, 0, 1),
            new DamageLifeBoost("Heavenly Blessing", 4, 2, 4));

    private final int damageIncrease;
    private final int lifeIncrease;

    public DamageLifeBoost(String name, int damageIncrease, int lifeIncrease, int cost) {
        this(UUID.randomUUID(), name, damageIncrease, lifeIncrease, cost);
    }

    public DamageLifeBoost(UUID id, String name, int damageIncrease, int lifeIncrease, int cost) {
        super(id, name, cost);

        this.damageIncrease = damageIncrease;
        this.lifeIncrease = lifeIncrease;
    }

    public int damageIncrease() {
        return damageIncrease;
    }

    public int lifeIncrease() {
        return lifeIncrease;
    }

    @Override
    public State use(State state, List<ITargetable> targets) {
        List<State.PlayerInfo> playersInfo = state.getPlayersInfo();

        targets.stream()
                .map(t -> ((IAttachable) t).attach(this))
                .forEach(t -> {
                    State.PlayerInfo p = playersInfo.stream()
                            .filter(_p -> _p.field.contains(t))
                            .findFirst()
                            .get();
                    List<ICard> fieldCards = p.field.getCards();

                    int indexOfCard = fieldCards.indexOf(t);
                    fieldCards.remove(indexOfCard);
                    fieldCards.add(indexOfCard, t);
                });

        return null;
    }

    @Override
    public void validUseOrRaisesException(State state, List<ITargetable> targets) throws MagicException {
        if (targets.stream().anyMatch(t -> !(t instanceof IAttachable))) {
            throw new IllegalCardUsageException("can only attach to attachable entity");
        }

        if (targets.stream().anyMatch(t ->
                state.getPlayersInfo().stream().noneMatch(p ->
                        p.field.contains((Card) t)))) {
            throw new IllegalCardUsageException("can only attach to a card in the field");
        }
    }

    @Override
    public ICard duplicate() {
        return new DamageLifeBoost(name(), damageIncrease, lifeIncrease, cost());
    }

    @Override
    public String toString(boolean longDescription) {
        return super.toString(longDescription) +
                String.format(" d+:%d l+: %d", damageIncrease(), lifeIncrease());
    }
}
