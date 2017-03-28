package hearthstone.core;

import hearthstone.core.cards.Card;
import hearthstone.core.cards.Lackey;
import hearthstone.core.cards.magics.AreaBurn;
import hearthstone.core.cards.magics.Burn;
import hearthstone.core.cards.magics.Effect;

import java.util.*;

/**
 * Cards.
 * <p>
 * Hides a collection of cards. Can be used to represent
 * decks, hands, fields and graveyards.
 */
public class Cards {

    public static final Cards EMPTY = new Cards();

    private List<Card> cards;

    public Cards() {
        this(new ArrayList<>());
    }

    public Cards(Card[] cards) {
        this(Arrays.asList(cards));
    }

    public Cards(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public static Cards random(int numberOfCardsInNewDeck, Random random) {
        List<Card> cardsAvailable = new ArrayList<>();

        cardsAvailable.addAll(Lackey.DEFAULT_CARDS);
        cardsAvailable.addAll(Burn.DEFAULT_CARDS);
        cardsAvailable.addAll(AreaBurn.DEFAULT_CARDS);
        cardsAvailable.addAll(Effect.DEFAULT_CARDS);

        return random(cardsAvailable, numberOfCardsInNewDeck, random);
    }

    public static Cards random(List<Card> possibleCards, int numberOfCardsInNewDeck, Random random) {
        LinkedList<Card> cardsInNewDeck = new LinkedList<>();

        for (int i = 0; i < numberOfCardsInNewDeck; i++) {
            cardsInNewDeck.add(possibleCards.get(random.nextInt(possibleCards.size())));
        }

        return new Cards(cardsInNewDeck);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public boolean contains(Card card) {
        return cards.contains(card);
    }

    public int indexOf(Card card) {
        return cards.indexOf(card);
    }

    public Cards removeAll(List<Card> cards) {
        List<Card> newCards = getCards();
        newCards.removeAll(cards);
        return new Cards(newCards);
    }

    public Cards addAll(List<Card> cards) {
        List<Card> newCards = getCards();
        newCards.addAll(cards);
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
        return cards.toString();
    }
}
