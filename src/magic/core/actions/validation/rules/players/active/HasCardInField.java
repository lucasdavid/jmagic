package magic.core.actions.validation.rules.players.active;

import magic.core.cards.ICard;
import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;


public class HasCardInField extends ValidationRule {

    private final ICard card;

    public HasCardInField(ICard card) {
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
