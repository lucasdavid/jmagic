package magic.core.actions;

import magic.infrastructure.validation.IGameModifier;

import java.util.Objects;

/**
 * Action Base Class.
 * <p>
 * Basic interface for a {@link magic.core.Player}'s action.
 *
 * @author ldavid
 */
public abstract class Action implements IGameModifier {

    @Override
    public boolean equals(Object o) {
        return o != null && getClass().equals(o.getClass());
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
