package hearthstone.core.cards.magics;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class AreaBurn extends Burn {

    public static Collection<AreaBurn> DEFAULT_CARDS
            = Collections.unmodifiableCollection(Arrays.asList(
            new AreaBurn("Fire Rain", 2, 2),
            new AreaBurn("Great Flood", 4, 10)
    ));

    public AreaBurn(String name, int damage, int manaCost) {
        super(name, damage, manaCost);
    }

    public AreaBurn(UUID id, String name, int damage, int manaCost) {
        super(id, name, damage, manaCost);
    }

    @Override
    public String toString() {
        return String.format("%s: deals %d of damage to all enemies (mana cost: %d)",
                getName(), getDamage(), getManaCost());
    }
}
