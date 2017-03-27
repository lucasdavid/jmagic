package hearthstone.core.cards;

import hearthstone.core.ITargetable;
import hearthstone.core.State;

import java.util.*;

public class Lackey extends Card {

    private final int attack;
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
        this(name, attack, life, life, manaCost);
    }

    public Lackey(String name, int attack, int life, int maxLife, int manaCost) {
        this(UUID.randomUUID(), name, attack, life, maxLife, manaCost);
    }

    public Lackey(String name, int attack, int life, int manaCost, Collection<LackeyAttributes> buffs) {
        this(name, attack, life, life, manaCost, buffs);
    }

    public Lackey(String name, int attack, int life, int maxLife, int manaCost, Collection<LackeyAttributes> buffs) {
        this(UUID.randomUUID(), name, attack, life, maxLife, manaCost, buffs);
    }

    public Lackey(UUID id, String name, int attack, int life, int manaCost) {
        this(id, name, attack, life, life, manaCost);
    }

    public Lackey(UUID id, String name, int attack, int life, int maxLife, int manaCost) {
        this(id, name, attack, life, maxLife, manaCost, new ArrayList<>());
    }

    public Lackey(UUID id, String name, int attack, int life, int maxLife, int manaCost, Collection<LackeyAttributes> buffs) {
        super(id, name, manaCost);

        this.attack = attack;
        this.life = life;
        this.maxLife = maxLife;
        this.buffs = new ArrayList<>(buffs);
    }

    @Override
    public State use(State state, List<ITargetable> targets) {
        return null;
    }

    public Collection<LackeyAttributes> getBuffs() {
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
}
