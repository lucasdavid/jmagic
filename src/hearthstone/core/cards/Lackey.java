package hearthstone.core.cards;

import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class Lackey extends Card {

    private final int attack;
    private final int life;
    private final int maxLife;
    private final Collection<Buff> buffs;

    public static final Collection<Lackey> DEFAULT_CARDS = Collections.unmodifiableList(Arrays.asList(
            new Lackey("Frodo Baggins", 2, 1, 1),
            new Lackey("Aragorn", 5, 7, 6, Arrays.asList(Buff.PROVOKE)),
            new Lackey("Legolas", 8, 4, 6, Arrays.asList(Buff.WIND_FURY)),
            new Lackey("Sauron", 10, 10, 10, Arrays.asList(Buff.values()))
    ));

    public Lackey(String name, int attack, int life, int manaCost) {
        this(name, attack, life, life, manaCost);
    }

    public Lackey(String name, int attack, int life, int maxLife, int manaCost) {
        this(UUID.randomUUID(), name, attack, life, maxLife, manaCost);
    }

    public Lackey(String name, int attack, int life, int manaCost, Collection<Buff> buffs) {
        this(name, attack, life, life, manaCost, buffs);
    }

    public Lackey(String name, int attack, int life, int maxLife, int manaCost, Collection<Buff> buffs) {
        this(UUID.randomUUID(), name, attack, life, maxLife, manaCost, buffs);
    }

    public Lackey(UUID id, String name, int attack, int life, int manaCost) {
        this(id, name, attack, life, life, manaCost);
    }

    public Lackey(UUID id, String name, int attack, int life, int maxLife, int manaCost) {
        this(id, name, attack, life, maxLife, manaCost, new ArrayList<>());
    }

    public Lackey(UUID id, String name, int attack, int life, int maxLife, int manaCost, Collection<Buff> buffs) {
        super(id, name, manaCost);

        this.attack = attack;
        this.life = life;
        this.maxLife = maxLife;
        this.buffs = new ArrayList<>(buffs);
    }

    public Collection<Buff> getBuffs() {
        return new ArrayList<>(buffs);
    }

    @Override
    public String toString() {
        return (life == maxLife
                ? String.format("%s (atk %d/def %d)", super.toString(), attack, life)
                : String.format("%s (atk %d/def %d from %d)", super.toString(), attack, life, maxLife))
                + (buffs.isEmpty()
                ? String.format(" {%s}", buffs)
                : "");
    }

    @Override
    public Lackey copy() {
        return new Lackey(getId(), getName(), attack, life, maxLife, getManaCost(), buffs);
    }

}
