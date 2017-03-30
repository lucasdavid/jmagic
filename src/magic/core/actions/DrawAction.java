package magic.core.actions;

import magic.core.cards.Cards;
import magic.core.State;
import magic.core.State.PlayerInfo;
import magic.core.actions.validation.CurrentPlayerHasCardsInTheirDeck;
import magic.core.actions.validation.HasNotAlreadyDrawnInThisTurn;
import magic.core.actions.validation.ValidationRule;
import magic.core.contracts.ICard;

import java.util.Collection;
import java.util.List;

/**
 * Draw Action.
 *
 * Current player draws a card from their deck.
 *
 * @author ldavid
 */
public final class DrawAction extends Action {

    @Override
    public State update(State state) {
        List<PlayerInfo> playersInfo = state.getPlayersInfo();
        PlayerInfo p = playersInfo.remove(state.turnsCurrentPlayerId);

        List<ICard> hand = p.hand.getCards();
        List<ICard> deck = p.deck.getCards();

        hand.add(deck.remove(0));

        playersInfo.add(state.turnsCurrentPlayerId,
                new PlayerInfo(p.player, p.life, p.maxLife,
                        new Cards(deck), new Cards(hand), p.field, p.graveyard));

        return new State(playersInfo, state.turn, state.done,
                state.turnsCurrentPlayerId, this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return List.of(
                new CurrentPlayerHasCardsInTheirDeck(),
                new HasNotAlreadyDrawnInThisTurn());
    }
}
