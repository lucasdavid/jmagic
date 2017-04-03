package magic.core.actions.validation;

import magic.core.State;

public class CurrentPlayerHasCardsInTheirDeck extends ValidationRule {

    private final String message;

    public CurrentPlayerHasCardsInTheirDeck() {
        this("cannot draw from empty deck");
    }

    public CurrentPlayerHasCardsInTheirDeck(String message) {
        this.message = message;
    }

    @Override
    public void onValidate(State state) {
        if (state.currentPlayerState().deck.isEmpty()) {
            errors.add(message);
        }
    }
}
