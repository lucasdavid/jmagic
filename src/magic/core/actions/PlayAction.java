package magic.core.actions;

import magic.core.cards.Cards;
import magic.core.State;
import magic.core.State.PlayerInfo;
import magic.core.cards.Card;
import magic.core.exceptions.MagicException;
import magic.core.exceptions.InvalidActionException;

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
        PlayerInfo p = playersInfo.remove(state.turnsCurrentPlayerId);
        List<Card> hand = p.hand.getCards();
        List<Card> field = p.field.getCards();

        hand.remove(card);
        field.add(card);

        p = new PlayerInfo(p.player, p.life, p.maxLife,
                p.deck, new Cards(hand), new Cards(field), p.graveyard);
        playersInfo.add(state.turnsCurrentPlayerId, p);

        return new State(playersInfo, state.turn, state.done, state.turnsCurrentPlayerId,
                this, state);
    }

    @Override
    public void raiseForErrors(State state) throws MagicException {
        if (!state.currentPlayerInfo().hand.contains(card)) {
            throw new InvalidActionException("card isn't in the player's hand");
        }
    }
}
