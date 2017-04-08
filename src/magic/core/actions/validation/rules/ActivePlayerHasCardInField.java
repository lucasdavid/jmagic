package magic.core.actions.validation.rules;

import magic.core.actions.validation.ValidationRule;
import magic.core.cards.ICard;
import magic.core.states.State;

public class ActivePlayerHasCardInField extends ValidationRule {

    private final ICard card;

    public ActivePlayerHasCardInField(ICard card) {
        this.card = card;
    }

    @Override
    public void onValidate(State state) {
        State.PlayerState activeState = state.activePlayerState();
        if (activeState.field.contains(card)) {
            errors.add(String.format("{%s} doesn't have {%s} in their field",
                activeState.player, card));
        }
    }
}
