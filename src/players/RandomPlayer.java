package players;

import core.players.Player;
import core.Action;
import core.State;
import core.deck.Deck;

public class RandomPlayer extends Player {

    public RandomPlayer(Deck deck) {
        super(deck);
    }

    public RandomPlayer(String name, Deck deck) {
        super(name, deck);
    }

    @Override
    public Action act(State state) {
        return null;
    }
}
