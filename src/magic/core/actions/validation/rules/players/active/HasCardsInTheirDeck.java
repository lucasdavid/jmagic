package magic.core.actions.validation.rules.players.active;

import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;


public class HasCardsInTheirDeck extends ValidationRule {

    private final int count;

    public HasCardsInTheirDeck() {
        this(1);
    }

    public HasCardsInTheirDeck(int count) {
        this.count = count;
    }

    @Override
    public void onValidate(State state) {
        State.PlayerState activeState = state.activePlayerState();

        if (activeState.deck.size() < count) {
            errors.add(String.format(
                "{%s} cannot draw because their deck has less than %d cards",
                activeState.player, count));
        }
    }
}
