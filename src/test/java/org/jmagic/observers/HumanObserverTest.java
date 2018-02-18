package org.jmagic.observers;

import org.jmagic.actions.AdvanceGameAction;
import org.jmagic.actions.DrawAction;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.states.State;
import org.jmagic.players.RandomPlayer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.Math.abs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class HumanObserverTest {

    private final static double
        WAITING_TIME = .6,
        EPSILON = .01;

    private final long STARTED_AT = 3201293;
    private final State INITIAL_STATE = new State(
        List.of(new RandomPlayer("test-1"), new RandomPlayer("test-2")),
        List.of(Cards.EMPTY, Cards.EMPTY),
        20);

    @Test
    void afterPlayerAct() {
        Observer o = new HumanObserver(WAITING_TIME);

        long start = System.currentTimeMillis();
        State actual = o.afterPlayerAct(INITIAL_STATE, new AdvanceGameAction(), STARTED_AT, STARTED_AT + 990);
        long elapsed = System.currentTimeMillis() - start;

        assertTrue(String.format("elapsed time is less than %f: %f", EPSILON, elapsed / 1000.),
            elapsed <= EPSILON * 1000);
        assertEquals(INITIAL_STATE, actual);

        start = System.currentTimeMillis();
        actual = o.afterPlayerAct(INITIAL_STATE, new DrawAction(), STARTED_AT, STARTED_AT + 990);
        elapsed = System.currentTimeMillis() - start;

        assertTrue(String.format("elapsed time close to %f seconds: %f", WAITING_TIME, elapsed / 1000.),
            abs(elapsed - WAITING_TIME * 1000) <= EPSILON * 1000);
        assertEquals(INITIAL_STATE, actual);
    }

}
