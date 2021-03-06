package org.jmagic.observers;

import org.jmagic.actions.Action;
import org.jmagic.actions.Disqualify;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.RandomPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LooseOnNullActionTest {
    @BeforeEach
    void setUp() {
    }

    @Test
    void afterPlayerAct() {
        State state = new State(
                List.of(new RandomPlayer("test-1"), new RandomPlayer("test-2")),
                List.of(Cards.EMPTY, Cards.EMPTY),
            20);

        Action validAction = new Action() {
            @Override
            public State update(State state) {
                return state;
            }

            @Override
            public ValidationRule validationRules() {
                return null;
            }
        };

        State actual = new LooseOnNullAction().afterPlayerAct(state, validAction, 0, 100);
        assertEquals(state, actual);

        actual = new LooseOnNullAction().afterPlayerAct(state, null, 0, 100);
        assertNotEquals(state, actual);
        assertTrue(actual.actionThatLedToThisState instanceof Disqualify, "Didn't disqualified player as expected");
    }
}
