package org.games.jmagic.actions.validation.rules.players.active;

import org.games.jmagic.core.cards.ICard;
import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;


public class HasCardInHand extends ValidationRule {

    private final ICard card;

    public HasCardInHand(ICard card) {
        this.card = card;
    }

    @Override
    public void onValidate(State state) {
        State.PlayerState activeState = state.activePlayerState();
        if (!activeState.hand.contains(card)) {
            errors.add(String.format("%s doesn't have {%s} in their hand",
                activeState.player, card));
        }
    }
}
