package magic.core.actions;

import magic.core.Player;
import magic.core.State;
import magic.core.State.PlayerState;
import magic.core.actions.validation.ActorIsCurrentPlayer;
import magic.core.actions.validation.PlayerHasMoreThan7CardsInTheirHand;
import magic.core.actions.validation.ValidationRule;
import magic.core.cards.Cards;
import magic.core.contracts.cards.ICard;

import java.util.Collection;
import java.util.List;

/**
 * Discard Action.
 *
 * Current player discards a card from their hand.
 *
 * @author ldavid
 */
public final class DiscardAction extends Action {

    private final ICard card;

    public DiscardAction(ICard card) {
        this.card = card;
    }

    @Override
    public State update(State state, Player actor) {
        List<PlayerState> playersInfo = state.playerStates();
        PlayerState p = playersInfo.remove(state.turnsCurrentPlayerIndex);

        List<ICard> hand = p.hand.cards();
        List<ICard> graveyard = p.graveyard.cards();

        graveyard.add(hand.remove(hand.indexOf(card)));

        playersInfo.add(state.turnsCurrentPlayerIndex,
                new PlayerState(p.player, p.life(), p.maxLife(),
                        p.deck, new Cards(hand), p.field, new Cards(graveyard)));

        return new State(playersInfo, state.turn, state.done,
                state.turnsCurrentPlayerIndex, this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return List.of(
                new ActorIsCurrentPlayer(),
                new PlayerHasMoreThan7CardsInTheirHand());
    }
}
