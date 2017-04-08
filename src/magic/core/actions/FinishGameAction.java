package magic.core.actions;

import magic.core.actions.validation.ValidationRule;
import magic.core.actions.validation.rules.GameIsNotDone;
import magic.core.states.State;

import java.util.Collection;
import java.util.List;

/**
 * Finish a game.
 *
 * @author ldavid
 */
public class FinishGameAction extends Action {

    @Override
    public State update(State state) {
        return new State(state.playerStates(), state.turn, state.step, true,
                state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return List.of(
            new GameIsNotDone());
    }
}
