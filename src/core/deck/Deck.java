package core.deck;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import core.cards.Card;
import core.cards.Lackey;
import core.cards.Magic;

public class Deck {

    Collection<Card> cards;

    Deck(Card[] cards) {
        this(Arrays.asList(cards));
    }

    Deck(Collection<Card> cards) {
        this.cards = cards.stream()
                .collect(Collectors.toList());
    }

    Collection<Card> getCards() {
        return cards.stream()
                .map(c -> c.copy())
                .collect(Collectors.toList());
    }

    public static Deck random(int numberOfCardsInNewDeck, Random random) {
        List<Card> cardsAvailable = Stream
                .concat(Lackey.DEFAULT_CARDS.stream(), Magic.DEFAULT_CARDS.stream())
                .collect(Collectors.toList());

        return random(cardsAvailable, numberOfCardsInNewDeck, random);
    }

    public static Deck random(List<Card> possibleCards, int numberOfCardsInNewDeck, Random random) {
        LinkedList<Card> cardsInNewDeck = new LinkedList<>();

        for (int i = 0; i < numberOfCardsInNewDeck; i++) {
            cardsInNewDeck.add(possibleCards.get(random.nextInt(possibleCards.size())));
        }

        return new Deck(cardsInNewDeck);
    }

    @Override
    public String toString() {
        return "  " + String.join("\n  ", cards.stream()
                .map(c -> c.toString())
                .toArray(size -> new String[size]));
    }

}
