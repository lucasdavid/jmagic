package magic.core.actions;

import magic.core.actions.validation.ValidationRule;
import magic.core.actions.validation.rules.ActiveAndTurnsPlayersAreTheSame;
import magic.core.actions.validation.rules.ActivePlayerHasCardsInTheirDeck;
import magic.core.actions.validation.rules.ActivePlayerHasNotAlreadyDrawnInThisTurn;
import magic.core.actions.validation.rules.TurnsStepIs;
import magic.core.cards.Cards;
import magic.core.cards.ICard;
import magic.core.states.State;
import magic.core.states.State.PlayerState;
import magic.core.states.TurnStep;

import java.util.Collection;
import java.util.List;

/**
 * Draw Action.
 * <p>
 * Current player draws a card from their deck.
 *
 * @author ldavid
 */
public final class DrawAction extends Action {

    @Override
    public State update(State state) {
        List<PlayerState> playersInfo = state.playerStates();
        State.PlayerState p = playersInfo.remove(state.turnsPlayerIndex);

        List<ICard> hand = p.hand.cards();
        List<ICard> deck = p.deck.cards();

        hand.add(deck.remove(0));

        playersInfo.add(state.turnsPlayerIndex,
            new State.PlayerState(p.player, p.life(), p.maxLife(),
                new Cards(deck), new Cards(hand), p.field, p.graveyard));

        return new State(playersInfo, state.turn, state.step, state.done,
            state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return List.of(
            new TurnsStepIs(TurnStep.DRAW),
            new ActiveAndTurnsPlayersAreTheSame(),
            new ActivePlayerHasCardsInTheirDeck(),
            new ActivePlayerHasNotAlreadyDrawnInThisTurn());
    }
}
