package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;

public class PlayerHasCardsInTheirDeck extends ValidationRule {

    @Override
    public void onValidate(State state, Player actor) {
        if (state.playerState(actor).deck.isEmpty()) {
            errors.add(String.format("%s cannot draw because their deck is empty", actor));
        }
    }
}
