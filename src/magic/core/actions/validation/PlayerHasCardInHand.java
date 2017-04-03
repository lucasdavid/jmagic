package magic.core.actions.validation;

import magic.core.State;
import magic.core.contracts.ICard;

public class PlayerHasCardInHand extends ValidationRule {

    private final ICard card;

    public PlayerHasCardInHand(ICard card) {
        this.card = card;
    }

    @Override
    public void onValidate(State state) {
        if (state.currentPlayerState().hand.contains(card)) {
            errors.add(String.format("Player {%s} doesn't have {%s} in their field",
                    state.currentPlayerState(), card));
        }
    }
}
