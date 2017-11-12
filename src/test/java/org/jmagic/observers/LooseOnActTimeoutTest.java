package org.jmagic.observers;

import org.jmagic.actions.DisqualifyAction;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.states.State;
import org.jmagic.players.RandomPlayer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.*;

class LooseOnActTimeoutTest {

    final double ACTION_TIMEOUT = 1.0;
    final long ACTION_STARTED_AT = 3201293;

    final long VALID_ACTION_ENDED_AT = ACTION_STARTED_AT + Math.round(1000 * ACTION_TIMEOUT) - 10;
    final long INVALID_ACTION_ENDED_AT = ACTION_STARTED_AT + Math.round(1000 * ACTION_TIMEOUT) + 10;

    private final State INITIAL_STATE = new State(
            List.of(new RandomPlayer("test-1"), new RandomPlayer("test-2")),
            List.of(Cards.EMPTY, Cards.EMPTY),
            20);

    @Test
    void afterPlayerAct() {
        Observer o = new LooseOnActTimeout(ACTION_TIMEOUT);

        State actual = o.afterPlayerAct(INITIAL_STATE, null, ACTION_STARTED_AT, VALID_ACTION_ENDED_AT);
        assertEquals(INITIAL_STATE, actual);

        actual = o.afterPlayerAct(INITIAL_STATE, null, ACTION_STARTED_AT, INVALID_ACTION_ENDED_AT);
        assertNotEquals(INITIAL_STATE, actual);
        assertTrue(actual.actionThatLedToThisState instanceof DisqualifyAction);
    }

}