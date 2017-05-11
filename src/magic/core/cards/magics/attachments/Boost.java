package magic.core.cards.magics.attachments;

import magic.infrastructure.validation.rules.ValidationRule;
import magic.core.cards.Properties;
import magic.core.states.State;
import magic.core.cards.Card;
import magic.core.cards.lands.BasicLands;
import magic.core.cards.ICard;
import magic.core.ITargetable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static magic.infrastructure.validation.connectives.Connectives.And;

public class Boost extends Card implements IDamageBoost, ILifeBoost {

    private final int damageIncrease;
    private final int lifeIncrease;

    public Boost(String name, int damageIncrease, int lifeIncrease, Collection<BasicLands> cost) {
        this(UUID.randomUUID(), name, damageIncrease, lifeIncrease, Collections.emptySet(), cost);
    }

    public Boost(String name, int damageIncrease, int lifeIncrease,
                 Collection<Properties> properties, Collection<BasicLands> cost) {
        this(UUID.randomUUID(), name, damageIncrease, lifeIncrease, properties, cost);
    }

    public Boost(UUID id, String name, int damageIncrease, int lifeIncrease,
                 Collection<Properties> properties, Collection<BasicLands> cost) {
        super(id, name, properties, cost);

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
    public State update(State state) {
//        List<State.PlayerState> playersInfo = state.playerStates();
//
//        targets.stream()
//                .map(t -> ((IAttachable) t).attach(this))
//                .forEach(t -> {
//                    State.PlayerState p = playersInfo.stream()
//                            .filter(_p -> _p.field.contains(t))
//                            .findFirst()
//                            .get();
//                    List<ICard> fieldCards = p.field.cards();
//
//                    int indexOfCard = fieldCards.indexOf(t);
//                    fieldCards.remove(indexOfCard);
//                    fieldCards.add(indexOfCard, t);
//                });
        return null;
    }

//    @Override
//    public void raiseForErrors(State state, List<ITargetable> targets) throws JMagicException {
//        if (targets.stream().anyMatch(t -> !(t instanceof IAttachable))) {
//            throw new IllegalCardUsageException("can only attach to attachable entity");
//        }
//
//        if (targets.stream().anyMatch(t ->
//                state.playerStates().stream().noneMatch(p ->
//                        p.field.contains((ICard) t)))) {
//            throw new IllegalCardUsageException("can only attach to a card in the field");
//        }
//    }

    @Override
    public ICard duplicate() {
        return new Boost(name(), damageIncrease, lifeIncrease, cost());
    }

    @Override
    public String toString(boolean detailed) {
        return super.toString(detailed) +
                String.format(" d+:%d l+: %d", damageIncrease(), lifeIncrease());
    }

    @Override
    public ValidationRule validationRules() {
//        return And(
//            new TargetsAreAttachable(),
//            new TargetsAreInTheField()
//        );
        return null;
    }
}
