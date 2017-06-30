package org.games.jmagic.actions.validation.rules.players.active;

import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;


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
