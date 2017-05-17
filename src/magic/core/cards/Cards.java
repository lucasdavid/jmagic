package magic.core.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public ICard getValidated(ICard card) {
        return cards.get(cards.indexOf(card));
    }

    public int size() {
        return cards.size();
    }

    public Cards update(ICard... updatedCards) {
        return update(List.of(updatedCards));
    }

    public Cards update(List<ICard> updatedCards) {
        List<ICard> newCards = new ArrayList<>(cards);
        for (ICard card : updatedCards) {
            newCards.set(newCards.indexOf(card), card);
        }
        return new Cards(newCards);
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
        return toString(false);
    }

    public String toString(boolean detailed) {
        return cards.stream()
            .map(c -> c.toString(detailed))
            .collect(Collectors.toList())
            .toString();
    }
}
