package magic.core.cards.magics.attachments;

import magic.core.states.State;
import magic.core.cards.Card;
import magic.core.cards.lands.BasicLands;
import magic.core.cards.ICard;
import magic.core.ITargetable;
import magic.core.exceptions.JMagicException;
import magic.core.exceptions.IllegalCardUsageException;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class DamageLifeBoost extends Card implements IDamageBoost, ILifeBoost {

    private final int damageIncrease;
    private final int lifeIncrease;

    public DamageLifeBoost(String name, int damageIncrease, int lifeIncrease, Collection<BasicLands> cost) {
        this(UUID.randomUUID(), name, damageIncrease, lifeIncrease, cost);
    }

    public DamageLifeBoost(UUID id, String name, int damageIncrease, int lifeIncrease, Collection<BasicLands> cost) {
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
        List<State.PlayerState> playersInfo = state.playerStates();

        targets.stream()
                .map(t -> ((IAttachable) t).attach(this))
                .forEach(t -> {
                    State.PlayerState p = playersInfo.stream()
                            .filter(_p -> _p.field.contains(t))
                            .findFirst()
                            .get();
                    List<ICard> fieldCards = p.field.cards();

                    int indexOfCard = fieldCards.indexOf(t);
                    fieldCards.remove(indexOfCard);
                    fieldCards.add(indexOfCard, t);
                });

        return null;
    }

    @Override
    public void raiseForErrors(State state, List<ITargetable> targets) throws JMagicException {
        if (targets.stream().anyMatch(t -> !(t instanceof IAttachable))) {
            throw new IllegalCardUsageException("can only attach to attachable entity");
        }

        if (targets.stream().anyMatch(t ->
                state.playerStates().stream().noneMatch(p ->
                        p.field.contains((Card) t)))) {
            throw new IllegalCardUsageException("can only attach to a card in the field");
        }
    }

    @Override
    public ICard duplicate() {
        return new DamageLifeBoost(name(), damageIncrease, lifeIncrease, cost());
    }

    @Override
    public String toString(boolean detailed) {
        return super.toString(detailed) +
                String.format(" d+:%d l+: %d", damageIncrease(), lifeIncrease());
    }
}
