package org.jmagic.players;

import org.jmagic.infrastructure.IAgent;

import java.util.Objects;
import java.util.UUID;

/**
 * Player Base Class.
 * <p>
 * Basic interface for a Hearth Stone's player. Check its sub-classes at
 * {@link org.jmagic.players} package.
 */
public abstract class Player implements IAgent {

    private final UUID id;
    private final String name;

    protected Player(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public Player(String name) {
        this(UUID.randomUUID(), name);
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        try {
            return this == o || id().equals(((Player) o).id);
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }
}
