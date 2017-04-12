package magic.core.actions.validation.rules.players.active;

import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

public class HasCardsInTheirDeck extends ValidationRule {

    @Override
    public void onValidate(State state) {
        State.PlayerState activeState = state.activePlayerState();
        if (activeState.deck.isEmpty()) {
            errors.add(String.format("{%s} cannot draw because their deck isFrom empty",
                activeState.player));
        }
    }
}
