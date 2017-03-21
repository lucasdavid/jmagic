package hearthstone.core.actions;

import hearthstone.core.State;
import hearthstone.core.State.PlayerInfo;
import hearthstone.core.cards.Card;
import hearthstone.core.cards.Cards;
import hearthstone.core.exceptions.HearthStoneException;
import hearthstone.core.exceptions.InvalidActionException;
import java.util.List;

/**
 *
 * @author ldavid
 */
public final class DrawAction extends Action {

    @Override
    public State update(State state) throws HearthStoneException {
        List<PlayerInfo> playerInfos = state.getPlayerInfos();
        PlayerInfo playerInfo = playerInfos.remove(state.turnsCurrentPlayerId);

        List<Card> hand = playerInfo.hand.getCards();
        List<Card> deck = playerInfo.deck.getCards();

        hand.add(deck.remove(0));

        playerInfo = new PlayerInfo(playerInfo.player, playerInfo.life,
                new Cards(deck), new Cards(hand),
                playerInfo.field, playerInfo.graveyard);
        playerInfos.add(state.turnsCurrentPlayerId, playerInfo);

        return new State(playerInfos, state.turn, state.done,
                state.turnsCurrentPlayerId, this, state);
    }

    @Override
    public void validActionOrRaisesException(State state) throws HearthStoneException {
        // Check if deck has cards to be drawn.
        if (state.turnsCurrentPlayerInfo().deck.isEmpty()) {
            throw new InvalidActionException("cannot draw from empty deck.");
        }

        // Validate that Player is drawing only once in a turn!
        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;

        while (previous != null && previous.turn == state.turn) {
            if (previous.turnsCurrentPlayerId == state.turnsCurrentPlayerId
                    && actionExecutedInPrevious instanceof DrawAction) {
                throw new InvalidActionException("cannot draw more than once in a turn.");
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }
    }
}
