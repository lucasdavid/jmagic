package magic.core.cards.magics;

import magic.core.cards.Harmful;
import magic.core.cards.lands.BasicLands;
import magic.core.contracts.ICard;

import java.util.Collection;
import java.util.UUID;

public class Burn extends Harmful {

    public Burn(String name, int damage, Collection<BasicLands> cost) {
        this(name, damage, 1, cost);
    }

    public Burn(String name, int damage, int maxTargetCount, Collection<BasicLands> cost) {
        this(UUID.randomUUID(), name, damage, maxTargetCount, cost);
    }

    public Burn(UUID id, String name, int damage, int maxTargetCount, Collection<BasicLands> cost) {
        super(id, name, damage, maxTargetCount, cost);
    }

    @Override
    public ICard duplicate() {
        return new Burn(name(), damage(), maxTargetCount(), cost());
    }

    @Override
    public String toString(boolean longDescription) {
        return String.format("%s d:%d (%d enemies)",
                super.toString(longDescription), maxTargetCount(), effectiveDamage());
    }
}
