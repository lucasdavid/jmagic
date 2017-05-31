package magic.core.cards.magics.attachments;

import magic.core.cards.Card;
import magic.core.cards.ICard;
import magic.core.cards.Properties;
import magic.core.cards.lands.BasicLands;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

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
    public ICard duplicate() {
        return new Boost(name(), damageIncrease, lifeIncrease, cost());
    }

    @Override
    public String toString(boolean detailed) {
        return super.toString(detailed) +
            String.format(" d+:%d l+: %d", damageIncrease(), lifeIncrease());
    }
}
