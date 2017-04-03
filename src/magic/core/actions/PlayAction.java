package magic.core.actions;

import magic.core.State;
import magic.core.State.PlayerState;
import magic.core.actions.validation.PlayerHasCardInHand;
import magic.core.actions.validation.PlayerHasLandsToPlay;
import magic.core.actions.validation.ValidationRule;
import magic.core.cards.Card;
import magic.core.cards.Cards;
import magic.core.contracts.ICard;

import java.util.Collection;
import java.util.List;

/**
 * Play Action.
 * <p>
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
        List<PlayerState> playerStates = state.playerStates();
        State.PlayerState p = playerStates.remove(state.turnsCurrentPlayerIndex);
        List<ICard> hand = p.hand.cards();
        List<ICard> field = p.field.cards();

        ICard validCard = hand.remove(hand.indexOf(card));
        field.add(validCard);

        // TODO: waste lands!

        p = new State.PlayerState(p.player, p.life, p.maxLife,
                p.deck, new Cards(hand), new Cards(field), p.graveyard);
        playerStates.add(state.turnsCurrentPlayerIndex, p);

        return new State(playerStates, state.turn, state.done, state.turnsCurrentPlayerIndex,
                this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return List.of(
                new PlayerHasCardInHand(card),
                new PlayerHasLandsToPlay(card));
    }
}
