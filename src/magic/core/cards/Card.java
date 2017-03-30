package magic.core.cards;

import magic.core.contracts.ICard;

import java.util.Objects;
import java.util.UUID;

public abstract class Card implements ICard {

    private final UUID id;
    private final String name;
    private final int cost;

    public Card(UUID id, String name, int cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
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
    public int cost() {
        return cost;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean longDescription) {
        return longDescription
                ? String.format("\"%s\" c:%d", name, cost)
                : String.format("\"%s\"", name);
    }

    @Override
    public boolean equals(Object o) {
        try {
            return id().equals(((Card) o).id());
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(id());
        return hash;
    }
}
