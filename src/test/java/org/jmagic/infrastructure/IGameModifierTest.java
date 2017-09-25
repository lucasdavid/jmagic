package org.jmagic.infrastructure;

import org.jmagic.infrastructure.exceptions.ValidationException;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameModifier Test.
 *
 * @author ldavid
 */
class IGameModifierTest {

    private static class IsOngoingGame extends ValidationRule {
        @Override
        public void onValidate(State state) {
            if (state.done) {
                errors.add("game is already finished");
            }
        }
    }

    private static class TestGameFinisher implements IGameModifier {

        @Override
        public State update(State state) {
            return new State(state.playerStates(), state.turn, state.step, true,
                    state.turnsPlayerIndex, state.activePlayerIndex);
        }

        @Override
        public ValidationRule validationRules() {
            return new IsOngoingGame();
        }
    }

    @Test
    void raiseForErrors() {
        State ongoingGame = new State(null, 0, TurnSteps.UNTAP, false, 0, 0),
                finishedGame = new State(null, 0, TurnSteps.UNTAP, true, 0, 0);

        assertThrows(ValidationException.class, () -> new TestGameFinisher().raiseForErrors(finishedGame));

        try {
            new TestGameFinisher().raiseForErrors(ongoingGame);
        } catch (ValidationException e) {
            fail("TestGameFinisher modifier should not raise errors for an ongoing game, " +
                    "as its only rule is that the game is not finished.");
        }
    }

    @Test
    void isValid() {
        State ongoingGame = new State(null, 0, TurnSteps.UNTAP, false, 0, 0),
                finishedGame = new State(null, 0, TurnSteps.UNTAP, true, 0, 0);

        assertTrue(new TestGameFinisher().isValid(ongoingGame));
        assertFalse(new TestGameFinisher().isValid(finishedGame));
    }
}
