package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;
import magic.core.contracts.cards.ICard;

public class PlayerHasCardInField extends ValidationRule {

    private final ICard card;

    public PlayerHasCardInField(ICard card) {
        this.card = card;
    }

    @Override
    public void onValidate(State state, Player actor) {
        if (state.playerState(actor).field.contains(card)) {
            errors.add(String.format("Player {%s} doesn't have {%s} in their field", actor, card));
        }
    }
}
