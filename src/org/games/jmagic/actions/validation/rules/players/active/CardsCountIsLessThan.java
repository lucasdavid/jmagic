package org.games.jmagic.actions.validation.rules.players.active;

import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;


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
