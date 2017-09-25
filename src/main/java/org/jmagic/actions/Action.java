package org.jmagic.actions;

import org.jmagic.infrastructure.IGameModifier;
import org.jmagic.players.Player;

import java.util.Objects;

/**
 * Action Base Class.
 * <p>
 * Basic interface for a {@link Player}'s action.
 *
 * @author ldavid
 */
public abstract class Action implements IGameModifier {

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass().equals(o.getClass());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash * Objects.hashCode(getClass());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
