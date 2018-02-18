package org.jmagic.actions;

import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.actions.validation.rules.game.GameIsFinished;
import org.jmagic.core.states.State;

import static org.jmagic.infrastructure.validation.rules.BasicRules.Not;

/**
 * Finish A Game Action.
 * <p>
 * Note: the game master should not allowed the players to perform this action.
 * This can be achieved by setting a {@link org.jmagic.observers.LooseOnIllegalActionAttempt}
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
    public ValidationRule validationRules() {
        return Not(new GameIsFinished());
    }
}
