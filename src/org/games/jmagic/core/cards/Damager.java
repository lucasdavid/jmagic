package org.games.jmagic.core.cards;

import org.games.jmagic.core.cards.lands.BasicLands;

import java.util.Collection;
import java.util.UUID;

public abstract class Damager extends Card {

    private final int damage;
    private final int maxTargetCount;

    Damager(UUID id, String name, int damage,
            Collection<Properties> properties,
            Collection<BasicLands> cost) {
        this(id, name, damage, 1, properties, cost);
    }

    Damager(UUID id, String name, int damage, int maxTargetCount,
            Collection<Properties> properties,
            Collection<BasicLands> cost) {
        super(id, name, properties, cost);
        this.damage = damage;
        this.maxTargetCount = maxTargetCount;
    }

    public int damage() {
        return damage;
    }

    /**
     * @return the base damage plus any increase/decrease
     * generated by any game mechanics such as attachments.
     */
    public int effectiveDamage() {
        return damage();
    }

    public int maxTargetCount() {
        return maxTargetCount;
    }
}