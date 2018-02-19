package org.jmagic.experts;

import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.ICard;
import org.jmagic.core.states.State;
import org.jmagic.players.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public class CardFinderExpert implements IExpert {

    private final ICard card;

    public CardFinderExpert(ICard card) {
        this.card = card;
    }

    public ICard find(State state) throws NoSuchElementException {
        for (State.PlayerState s: state.playerStates()) {
            for (Cards c : List.of(s.field, s.hand)) {
                try {
                    return c.validated(card);
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }

        throw new NoSuchElementException(String.format("cannot find card %s in %s", card, state));
    }

    public Collection<ICard> findAll(State state) throws NoSuchElementException {
        List<ICard> validated = new ArrayList<>();

        for (State.PlayerState s: state.playerStates()) {
            for (Cards c : List.of(s.field, s.hand)) {
                try {
                    validated.add(c.validated(card));
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }

        return validated;
    }

    @Override
    public int count(State state, Player player) {
        return findAll(state).size();
    }

}
