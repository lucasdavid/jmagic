package magic.core.actions;

import magic.core.actions.validation.ValidationRule;
import magic.core.actions.validation.rules.GameIsFinished;
import magic.core.states.State;

import static magic.core.actions.validation.ValidationRules.Not;

/**
 * Finish A Game Action.
 * <p>
 * Note: the game master should not allowed the players to perform this action.
 * This can be achieved by setting a {@link magic.core.observers.LooseOnIllegalActionAttempt}
 * observer when building the game and not passing this class in the collection of allowed classes.
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
    protected ValidationRule validationRules() {
        return Not(new GameIsFinished());
    }
}
