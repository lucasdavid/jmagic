package magic.core.actions;

import magic.core.Player;
import magic.core.actions.validation.ActorIsCurrentPlayer;
import magic.core.cards.Cards;
import magic.core.State;
import magic.core.State.PlayerState;
import magic.core.actions.validation.PlayerHasCardsInTheirDeck;
import magic.core.actions.validation.HasNotAlreadyDrawnInThisTurn;
import magic.core.actions.validation.ValidationRule;
import magic.core.contracts.cards.ICard;

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
    public State update(State state, Player actor) {
        List<PlayerState> playersInfo = state.playerStates();
        State.PlayerState p = playersInfo.remove(state.turnsCurrentPlayerIndex);

        List<ICard> hand = p.hand.cards();
        List<ICard> deck = p.deck.cards();

        hand.add(deck.remove(0));

        playersInfo.add(state.turnsCurrentPlayerIndex,
                new State.PlayerState(p.player, p.life(), p.maxLife(),
                        new Cards(deck), new Cards(hand), p.field, p.graveyard));

        return new State(playersInfo, state.turn, state.done,
                state.turnsCurrentPlayerIndex, this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return List.of(
                new ActorIsCurrentPlayer(),
                new PlayerHasCardsInTheirDeck(),
                new HasNotAlreadyDrawnInThisTurn());
    }
}
