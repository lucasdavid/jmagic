package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;

public class PlayerHasMoreThan7CardsInTheirHand extends ValidationRule {

    @Override
    public void onValidate(State state, Player actor) {
        if (state.currentPlayerState().hand.size() <= 7) {
            errors.add(String.format("%s doesn't have more than 7 cards in their hand", actor));
        }
    }
}
