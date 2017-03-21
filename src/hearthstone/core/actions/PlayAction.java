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
public class PlayAction extends Action {

    private final Card card;

    public PlayAction(Card card) {
        this.card = card;
    }

    @Override
    public State update(State state) throws HearthStoneException {
        List<PlayerInfo> infos = state.getPlayerInfos();
        PlayerInfo info = infos.remove(state.turnsCurrentPlayerId);
        List<Card> hand = info.hand.getCards();
        List<Card> field = info.field.getCards();

        hand.remove(card);
        field.add(card);

        info = new PlayerInfo(info.player, info.life,
                info.deck, new Cards(hand), new Cards(field), info.graveyard);
        infos.add(state.turnsCurrentPlayerId, info);

        return new State(infos, state.turn, state.done, state.turnsCurrentPlayerId, this, state);
    }

    @Override
    public void validActionOrRaisesException(State state) throws HearthStoneException {
        // Check if hand has cards to be played.
        if (state.turnsCurrentPlayerInfo().hand.isEmpty()) {
            throw new InvalidActionException("cannot play from empty hand.");
        }
    }
}
