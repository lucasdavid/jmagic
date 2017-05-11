package magic.core.actions.validation.rules.players.active;

import magic.core.cards.ICard;
import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;


public class HasCardInHand extends ValidationRule {

    private final ICard card;

    public HasCardInHand(ICard card) {
        this.card = card;
    }

    @Override
    public void onValidate(State state) {
        State.PlayerState activeState = state.activePlayerState();
        if (!activeState.hand.contains(card)) {
            errors.add(String.format("%s doesn't have {%s} in their hand",
                activeState.player, card));
        }
    }
}
