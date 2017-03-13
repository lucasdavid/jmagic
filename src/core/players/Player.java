package core.players;

import core.Action;
import core.State;
import core.deck.Deck;

public abstract class Player {

    private Deck deck;
    private String name;

    public Player(Deck deck) {
        this.deck = deck;
    }

    public Player(String name, Deck deck) {
        this(deck);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Deck getDeck() {
        return deck;
    }

    public abstract Action act(State state);

    @Override
    public String toString() {
        return String.format("Player %s", name);
    }
}
