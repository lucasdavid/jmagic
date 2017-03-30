package magic.core.cards.magics;

import magic.core.cards.Harmful;
import magic.core.contracts.ICard;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AreaBurn extends Harmful {

    public static Collection<AreaBurn> SAMPLES = List.of(
            );

    public AreaBurn(String name, int damage, int cost) {
        super(name, damage, cost);
    }

    public AreaBurn(UUID id, String name, int damage, int cost) {
        super(id, name, damage, cost);
    }

    @Override
    public ICard duplicate() {
        return new AreaBurn(name(), damage(), cost());
    }

    @Override
    public String toString(boolean longDescription) {
        return String.format("%s d:%d (all)",
                super.toString(longDescription), effectiveDamage());
    }
}
