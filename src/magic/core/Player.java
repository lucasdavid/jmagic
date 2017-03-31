package magic.core;

import magic.core.actions.Action;
import magic.core.contracts.IIdentifiable;
import magic.core.exceptions.MagicException;

import java.util.Objects;
import java.util.UUID;

/**
 * Player Base Class.
 * <p>
 * Basic interface for a Hearth Stone's player.
 * Check its sub-classes.
 */
public abstract class Player implements IIdentifiable {

    public static final String[] DEFAULT_PLAYER_NAMES = new String[]{
            "Jane", "John", "Michael", "Lilliam", "Hellen", "Gus", "Josephine", "Richard",
            "Kyle", "Wendy", "Lucas", "Barbara", "Joe", "Ceres", "Pat", "Maximilian", "Maria",
            "Meredith", "Tully", "Tath", "Nathan", "Norton", "Olaf", "Olga", "Patrick", "Parker",
            "Ruth", "Rick", "Rosa", "Rudy", "Rupert", "Sully", "Sigfried", "Simon", "Silvia",
            "Cecilia", "Ursula", "Usla"};

    private final UUID id;
    private final String name;

    public Player(String name) {
        this(UUID.randomUUID(), name);
    }

    public Player(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract Action act(State state) throws MagicException;

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
            return id.equals(((Player) o).id);
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
        return String.format("Player %s", name);
    }
}
