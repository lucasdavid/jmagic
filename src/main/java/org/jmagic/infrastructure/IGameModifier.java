package org.jmagic.infrastructure;

import org.jmagic.infrastructure.exceptions.ValidationException;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;

/**
 * Game Modifier Interface.
 * <p>
 * Defines an entity which usage can alter the game's state integrity and, therefore,
 * should be first verified.
 *
 * @author ldavid
 */
public interface IGameModifier {

    State update(State state);

    /**
     * @return the validation rules applied to this entity.
     */
    ValidationRule validationRules();

    /**
     * Checks if the application/usage of this entity will bring the game to an invalid
     * state and, in this case, throws an exception.
     * <p>
     * This contract should always be implemented in the implementations {@link IGameModifier},
     * as it dependents on the domain and logic of each and every specific action.
     *
     * @param state the state in which this action will be applied.
     * @throws ValidationException raised when the application/usage of this
     *                             entity results in an invalid state for the game.
     */
    default IGameModifier raiseForErrors(State state) throws ValidationException {
        validationRules().raiseForErrors(state);
        return this;
    }


    /**
     * Check whether or not the application/usage of this entity will result in a valid state.
     *
     * @param state the state that will have its integrity verified.
     * @return true, if the action's application results in a valid state. False, otherwise.
     */
    default boolean isValid(State state) {
        return validationRules().isValid(state);
    }

}
