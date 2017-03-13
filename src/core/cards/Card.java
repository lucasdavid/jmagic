package core.cards;

import java.util.UUID;

public abstract class Card {

    protected UUID id;
    protected String name;
    protected int manaCost;

    public Card(UUID id, String name, int manaCost) {
        this.id = id;
        this.name = name;
        this.manaCost = manaCost;
    }

    public Card(String name, int manaCost) {
        this(UUID.randomUUID(), name, manaCost);
    }

    UUID getId() {
        return id;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) cost: %d", name, id, manaCost);
    }

    public abstract Card copy();

}
