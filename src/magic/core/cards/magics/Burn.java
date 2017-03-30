package magic.core.cards.magics;

import magic.core.cards.SingleHarmful;
import magic.core.contracts.ICard;

import java.util.UUID;

public class Burn extends SingleHarmful {

    public Burn(String name, int damage, int cost) {
        super(name, damage, cost);
    }

    public Burn(UUID id, String name, int damage, int cost) {
        super(id, name, damage, cost);
    }

    @Override
    public ICard duplicate() {
        return new Burn(name(), damage(), cost());
    }

    @Override
    public String toString(boolean longDescription) {
        return String.format("%s d:%d",
                super.toString(longDescription), effectiveDamage());
    }
}
