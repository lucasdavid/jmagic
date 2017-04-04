package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;

public class PlayerHasLessThan8CardsInTheirHand extends ValidationRule {

    @Override
    public void onValidate(State state, Player actor) {
        if (state.currentPlayerState().hand.size() > 7) {
            errors.add(String.format("%s has more than 7 cards in their hand", actor));
        }
    }
}
