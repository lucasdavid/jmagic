package magic.core.actions.validation.rules;

import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

public class CardsCountInTurnsPlayerHandIsMoreThan extends ValidationRule {

    private final int count;

    public CardsCountInTurnsPlayerHandIsMoreThan(int count) {
        this.count = count;
    }

    @Override
    public void onValidate(State state) {
        State.PlayerState turnsPlayerState = state.turnsPlayerState();
        if (turnsPlayerState.hand.size() <= this.count) {
            errors.add(String.format("%s doesn't have more than %d cards in their hand",
                turnsPlayerState.player, this.count));
        }
    }
}
