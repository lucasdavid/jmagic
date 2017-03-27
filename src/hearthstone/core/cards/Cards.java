package hearthstone.core.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<Card> cardsAvailable = Stream
                .concat(Lackey.DEFAULT_CARDS.stream(), Magic.DEFAULT_CARDS.stream())
                .collect(Collectors.toList());

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
        return "  " + String.join("\n  ", cards.stream()
                .map(c -> c.toString())
                .collect(Collectors.toList()));
    }
}
