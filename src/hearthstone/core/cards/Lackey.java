package hearthstone.core.cards;

import hearthstone.core.IDamageable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class Lackey extends SingleHarmful implements IDamageable {

    private final int life;
    private final int maxLife;
    private final Collection<LackeyAttributes> buffs;

    public static final Collection<Lackey> DEFAULT_CARDS = Collections.unmodifiableCollection(Arrays.asList(
            new Lackey("Frodo Baggins", 2, 1, 1),
            new Lackey("Aragorn", 5, 7, 6, Arrays.asList(LackeyAttributes.PROVOKE)),
            new Lackey("Legolas", 8, 4, 6, Arrays.asList(LackeyAttributes.WIND_FURY)),
            new Lackey("Sauron", 10, 10, 10, Arrays.asList(LackeyAttributes.values()))
    ));

    public Lackey(String name, int attack, int life, int manaCost) {
        this(UUID.randomUUID(), name, attack, life, life, manaCost, Collections.emptyList());
    }

    public Lackey(String name, int attack, int life, int manaCost,
                  Collection<LackeyAttributes> buffs) {
        this(UUID.randomUUID(), name, attack, life, life, manaCost, buffs);
    }

    public Lackey(UUID id, String name, int attack, int life, int maxLife, int manaCost,
                  Collection<LackeyAttributes> buffs) {
        super(id, name, attack, manaCost);
        this.life = life;
        this.maxLife = maxLife;
        this.buffs = Collections.unmodifiableCollection(buffs);
    }

    @Override
    public IDamageable takeDamage(int damage) {
        return new Lackey(getId(), getName(), getDamage(), life - damage, maxLife, getManaCost(), buffs);
    }

    public Collection<LackeyAttributes> getBuffs() {
        return buffs;
    }

    @Override
    public String toString() {
        return (life == maxLife
                ? String.format("%s (atk %d/def %d)", super.toString(), getDamage(), life)
                : String.format("%s (atk %d/def %d from %d)", super.toString(), getDamage(), life, maxLife))
                + (buffs.isEmpty()
                ? String.format(" {%s}", buffs)
                : "");
    }
}
