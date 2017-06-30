package org.games.jmagic.actions.validation.rules.players;

import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;


public class ActiveAndTurnsPlayersAreTheSame extends ValidationRule {

    @Override
    public void onValidate(State state) {
        if (state.activePlayerIndex != state.turnsPlayerIndex) {
            errors.add(String.format("Active {%s} and turn's player {%s} are not the same",
                state.activePlayerState().player, state.turnsPlayerState().player));
        }
    }
}
