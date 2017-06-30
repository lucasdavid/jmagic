package org.games.jmagic.core.cards;

import org.games.jmagic.core.cards.lands.BasicLands;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class Burn extends Damager {

    public Burn(String name, int damage, Collection<BasicLands> cost) {
        this(name, damage, 1, cost);
    }

    public Burn(String name, int damage, int maxTargetCount, Collection<BasicLands> cost) {
        this(UUID.randomUUID(), name, damage, maxTargetCount, cost);
    }

    public Burn(UUID id, String name, int damage, int maxTargetCount, Collection<BasicLands> cost) {
        super(id, name, damage, maxTargetCount, Collections.emptySet(), cost);
    }

    @Override
    public ICard duplicate() {
        return new Burn(name(), damage(), maxTargetCount(), cost());
    }

    @Override
    public String toString(boolean detailed) {
        return String.format("%s d:%d (%d enemies)",
            super.toString(detailed), maxTargetCount(), effectiveDamage());
    }
}
