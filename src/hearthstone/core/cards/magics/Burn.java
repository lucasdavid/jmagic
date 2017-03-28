package hearthstone.core.cards.magics;

import hearthstone.core.cards.SingleHarmful;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class Burn extends SingleHarmful {

    public static Collection<Burn> DEFAULT_CARDS = Collections.unmodifiableCollection(Arrays.asList(
            new Burn("You shall not pass", 4, 4),
            new Burn("Telekinesis", 3, 2)));

    public Burn(String name, int damage, int manaCost) {
        super(name, damage, manaCost);
    }

    public Burn(UUID id, String name, int damage, int manaCost) {
        super(id, name, damage, manaCost);
    }

    @Override
    public String toString() {
        return String.format("%s: deals %d of damage to a single enemy (mana cost: %d)",
                getName(), getDamage(), getManaCost());
    }
}
