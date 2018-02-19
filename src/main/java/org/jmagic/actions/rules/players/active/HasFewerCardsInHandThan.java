package org.jmagic.actions.rules.players.active;

import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;


public class HasFewerCardsInHandThan extends ValidationRule {

    private final int count;

    public HasFewerCardsInHandThan(int count) {
        this.count = count;
    }

    @Override
    public void onValidate(State state) {
        State.PlayerState p = state.activePlayerState();
        if (p.hand.size() >= count) {
            errors.add(String.format("%s has more than or exactly %d count in their hand",
                p.player, count));
        }
    }
}
