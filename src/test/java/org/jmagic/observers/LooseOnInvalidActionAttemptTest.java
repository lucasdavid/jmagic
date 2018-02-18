package org.jmagic.observers;

import org.jmagic.actions.Action;
import org.jmagic.actions.DisqualifyAction;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.RandomPlayer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.jmagic.infrastructure.validation.rules.BasicRules.IsTrue;
import static org.junit.Assert.*;

class LooseOnInvalidActionAttemptTest {

    private final State INITIAL_STATE = new State(
        List.of(new RandomPlayer("test-1"), new RandomPlayer("test-2")),
        List.of(Cards.EMPTY, Cards.EMPTY),
        0);

    @Test
    void afterPlayerAct() {
        Observer o = new LooseOnInvalidActionAttempt();

        State actual = o.afterPlayerAct(INITIAL_STATE, new ValidAction(), 0, 100);
        assertEquals(INITIAL_STATE, actual);

        actual = o.afterPlayerAct(INITIAL_STATE, new InvalidAction(), 0, 100);
        assertNotEquals(INITIAL_STATE, actual);
        assertTrue(actual.actionThatLedToThisState instanceof DisqualifyAction);
    }

    private static class ValidAction extends Action {

        @Override
        public State update(State state) {
            return state;
        }

        @Override
        public ValidationRule validationRules() {
            return IsTrue(state -> true);
        }
    }

    private static class InvalidAction extends ValidAction {

        @Override
        public State update(State state) {
            return state;
        }

        @Override
        public ValidationRule validationRules() {
            return IsTrue(state -> false);
        }
    }

}