package hearthstone.core.actions;

import hearthstone.core.Cards;
import hearthstone.core.State;
import hearthstone.core.State.PlayerInfo;
import hearthstone.core.cards.Card;
import hearthstone.core.exceptions.HearthStoneException;
import hearthstone.core.exceptions.InvalidActionException;

import java.util.List;

/**
 * Play Action.
 *
 * Play a card from the player's hand.
 *
 * @author ldavid
 */
public class PlayAction extends Action {

    private final Card card;

    public PlayAction(Card card) {
        this.card = card;
    }

    @Override
    public State update(State state) {
        List<PlayerInfo> playersInfo = state.getPlayersInfo();
        PlayerInfo info = playersInfo.remove(state.turnsCurrentPlayerId);
        List<Card> hand = info.hand.getCards();
        List<Card> field = info.field.getCards();

        hand.remove(card);
        field.add(card);

        info = new PlayerInfo(info.player, info.life,
                info.deck, new Cards(hand), new Cards(field), info.graveyard);
        playersInfo.add(state.turnsCurrentPlayerId, info);

        return new State(playersInfo, state.turn, state.done, state.turnsCurrentPlayerId,
                this, state);
    }

    @Override
    public void validActionOrRaisesException(State state) throws HearthStoneException {
        if (!state.currentPlayerInfo().hand.contains(card)) {
            throw new InvalidActionException("card isn't in the player's hand");
        }
    }
}
