package magic.core.actions.validation;

import magic.core.State;
import magic.core.contracts.ICard;

public class PlayerHasCardInField extends ValidationRule {

    private final ICard card;

    public PlayerHasCardInField(ICard card) {
        this.card = card;
    }

    @Override
    public void onValidate(State state) {
        if (state.currentPlayerState().field.contains(card)) {
            errors.add(String.format("Player {%s} doesn't have {%s} in their field",
                    state.currentPlayerState(), card));
        }
    }
}
