package magic.core.actions;

import magic.core.State;
import magic.core.actions.validation.GameIsntDone;
import magic.core.actions.validation.ValidationRule;

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
        return new State(state.playerStates(), state.turn, true,
                state.turnsCurrentPlayerIndex, this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return List.of(
            new GameIsntDone()
        );
    }
}
