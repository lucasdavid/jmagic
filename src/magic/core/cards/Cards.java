package magic.core.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Cards.
 * <p>
 * Hides a collection of cards. Can be used to represent
 * decks, hands, fields and graveyards.
 */
public class Cards {

    public static final Cards EMPTY = new Cards();

    private List<ICard> cards;

    public Cards() {
        this(new ArrayList<>());
    }

    public Cards(ICard[] cards) {
        this(Arrays.asList(cards));
    }

    public Cards(List<ICard> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public List<ICard> cards() {
        return new ArrayList<>(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public boolean contains(ICard card) {
        return cards.contains(card);
    }

    public int indexOf(ICard card) {
        return cards.indexOf(card);
    }

    public Cards removeAll(List<ICard> cards) {
        List<ICard> newCards = cards();
        newCards.removeAll(cards);
        return new Cards(newCards);
    }

    public Cards addAll(List<ICard> cards) {
        List<ICard> newCards = cards();
        newCards.addAll(cards);
        return new Cards(newCards);
    }

    public int size() {
        return cards.size();
    }

    @Override
    public boolean equals(Object o) {
        try {
            return cards.equals(((Cards) o).cards);
        } catch (ClassCastException | NullPointerException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.cards);
        return hash;
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
