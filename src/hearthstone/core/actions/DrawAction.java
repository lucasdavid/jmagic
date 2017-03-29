package hearthstone.core.actions;

import hearthstone.core.State;
import hearthstone.core.State.PlayerInfo;
import hearthstone.core.cards.Card;
import hearthstone.core.Cards;
import hearthstone.core.exceptions.HearthStoneException;
import hearthstone.core.exceptions.InvalidActionException;

import java.util.List;

/**
 *
 * @author ldavid
 */
public final class DrawAction extends Action {

    @Override
    public State update(State state) {
        List<PlayerInfo> playersInfo = state.getPlayersInfo();
        PlayerInfo p = playersInfo.remove(state.turnsCurrentPlayerId);

        List<Card> hand = p.hand.getCards();
        List<Card> deck = p.deck.getCards();

        hand.add(deck.remove(0));

        p = new PlayerInfo(p.player, p.life, p.maxLife,
                new Cards(deck), new Cards(hand),
                p.field, p.graveyard);
        playersInfo.add(state.turnsCurrentPlayerId, p);

        return new State(playersInfo, state.turn, state.done,
                state.turnsCurrentPlayerId, this, state);
    }

    @Override
    public void validActionOrRaisesException(State state) throws HearthStoneException {
        // Check if deck has cards to be drawn.
        if (state.currentPlayerInfo().deck.isEmpty()) {
            throw new InvalidActionException("cannot draw from empty deck");
        }

        // Validate that Player is drawing only once in a turn!
        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;

        while (previous != null && previous.turn == state.turn) {
            if (previous.turnsCurrentPlayerId == state.turnsCurrentPlayerId
                    && actionExecutedInPrevious instanceof DrawAction) {
                throw new InvalidActionException("cannot draw more than once in a turn");
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }
    }
}
