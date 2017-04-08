package magic.core.actions.validation.rules;

import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

public class CardsCountInTurnsPlayersHandIsLessThan extends ValidationRule {

    private final int count;

    public CardsCountInTurnsPlayersHandIsLessThan(int count) {
        this.count = count;
    }

    @Override
    public void onValidate(State state) {
        State.PlayerState turnsPlayerState = state.turnsPlayerState();
        if (count < turnsPlayerState.hand.size()) {
            errors.add(String.format("%s has more than or exactly %d count in their hand",
                turnsPlayerState.player, count));
        }
    }
}
