package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;
import magic.core.contracts.cards.ICard;

public class PlayerHasCardInHand extends ValidationRule {

    private final ICard card;

    public PlayerHasCardInHand(ICard card) {
        this.card = card;
    }

    @Override
    public void onValidate(State state, Player actor) {
        if (state.playerState(actor).hand.contains(card)) {
            errors.add(String.format("%s doesn't have {%s} in their hand", actor, card));
        }
    }
}
