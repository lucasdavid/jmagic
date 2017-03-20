package hearthstone.core;

import hearthstone.core.actions.Action;
import hearthstone.core.exceptions.HearthStoneException;
import java.util.Objects;
import java.util.UUID;

public abstract class Player {

    public static final String[] DEFAULT_PLAYER_NAMES = new String[]{
        "Jane", "John", "Michael", "Lilliam", "Hellen", "Gus", "Josephine", "Richard",
        "Kyle", "Wendy", "Lucas", "Barbara", "Joe", "Ceres", "Pat", "Maximilian", "Maria",
        "Meredith", "Tully", "Tath", "Nathan", "Norton", "Olaf", "Olga", "Patrick", "Parker",
        "Ruth", "Rick", "Rosa", "Rudy", "Rupert", "Sully", "Sigfried", "Simon", "Silvia",
        "Cecilia", "Ursula", "Usla"};

    private final UUID id;
    private final String name;

    public Player() {
        this("Unnamed");
    }

    public Player(String name) {
        this(UUID.randomUUID(), name);
    }

    public Player(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract Action act(State state) throws HearthStoneException;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return id.equals(((Player) o).id);
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
