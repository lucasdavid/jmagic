package magic.core.actions.validation.rules;

import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

public class ActiveAndTurnsPlayersAreTheSame extends ValidationRule {

    @Override
    public void onValidate(State state) {
        if (state.activePlayerIndex != state.turnsPlayerIndex) {
            errors.add(String.format("Active {%s} and turn's current player {%s} are not the same",
                state.activePlayerState().player, state.turnsPlayerState().player));
        }
    }
}
