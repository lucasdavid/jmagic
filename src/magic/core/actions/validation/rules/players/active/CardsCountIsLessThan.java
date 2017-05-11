package magic.core.actions.validation.rules.players.active;

import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;


public class CardsCountIsLessThan extends ValidationRule {

    private final int count;

    public CardsCountIsLessThan(int count) {
        this.count = count;
    }

    @Override
    public void onValidate(State state) {
        State.PlayerState p = state.activePlayerState();
        if (count >= p.hand.size()) {
            errors.add(String.format("%s has more than or exactly %d count in their hand",
                p.player, count));
        }
    }
}
