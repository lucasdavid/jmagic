package core.cards;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public class Magic extends Card {

    protected int damage;
    protected Boolean affectsMany;

    public static Collection<Magic> DEFAULT_CARDS = Arrays.asList(
            new Magic("You shall not pass", 4, true, 7),
            new Magic("Telekinesis", 3, false, 2));

    public Magic(UUID id, String name, int damage, Boolean affectsMany, int manaCost) {
        super(id, name, manaCost);

        this.damage = damage;
        this.affectsMany = affectsMany;
    }

    public Magic(String name, int damage, Boolean affectsMany, int manaCost) {
        super(name, manaCost);

        this.damage = damage;
        this.affectsMany = affectsMany;
    }

    @Override
    public String toString() {
        return String.format("%s: deals %d of damage to %s", super.toString(),
                damage, affectsMany ? "multiple enemies" : "a single enemy");
    }

    @Override
    public Magic copy() {
        return new Magic(id, name, damage, affectsMany, manaCost);
    }

}
