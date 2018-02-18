package org.jmagic.observers;

import org.jmagic.actions.DisqualifyAction;
import org.jmagic.actions.FinishGameAction;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.players.RandomPlayer;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {
    @Test
    void beforePlayerAct() {
        State expected = new State(
            List.of(new RandomPlayer("test-1"), new RandomPlayer("test-2")),
            List.of(Cards.EMPTY, Cards.EMPTY),
            20);

        Observer o = new TestObserver();

        State actual = o.beforePlayerAct(expected);
        assertSame(expected, actual);
    }

    @Test
    void afterPlayerAct() {
        State expected = new State(
            List.of(new RandomPlayer("test-1"), new RandomPlayer("test-2")),
            List.of(Cards.EMPTY, Cards.EMPTY),
            20);

        Observer o = new TestObserver();

        State actual = o.afterPlayerAct(expected, null, 0, 0);
        assertSame(expected, actual);
    }

    @Test
    void disqualify() {
        State s = new State(
            List.of(new RandomPlayer("test-1"), new RandomPlayer("test-2")),
            List.of(Cards.EMPTY, Cards.EMPTY),
            20);

        Observer o = new TestObserver();

        State d = o.disqualify(s);
        assertTrue(d.actionThatLedToThisState instanceof DisqualifyAction);
        assertFalse(d.playerStates().get(0).playing);
    }

    @Test
    void finish() {
        State s = new State(
            List.of(new RandomPlayer("test-1"), new RandomPlayer("test-2")),
            List.of(Cards.EMPTY, Cards.EMPTY),
            20);

        Observer o = new TestObserver();

        State d = o.finish(s);
        assertTrue(d.actionThatLedToThisState instanceof FinishGameAction);
        assertTrue(d.done);
    }

    @Test
    void testInvalidApply() {
        State bothLost = new State(
            List.of(
                new State.PlayerState(new RandomPlayer("test-1"), 20, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, false),
                new State.PlayerState(new RandomPlayer("test-2"), 20, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, false)),
            1, TurnSteps.DRAW, false, 0, 0);

        Observer o = new TestObserver();
        State d = o.disqualify(bothLost);
        assertNull(d);
    }

    @Test
    void testInvalidApply1() {
        State bothLost = new State(
            List.of(
                new State.PlayerState(new RandomPlayer("test-1"), -2, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, false),
                new State.PlayerState(new RandomPlayer("test-2"), -1, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, true)),
            1, TurnSteps.DRAW, false, 0, 0);

        Observer o = new TestObserver();
        State d = o.disqualify(bothLost);
        assertNull(d);
    }

    @Test
    void testInvalidApply2() {
        State finishedGame = new State(
            List.of(
                new State.PlayerState(new RandomPlayer("test-1"), 20, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, true),
                new State.PlayerState(new RandomPlayer("test-2"), 20, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, true)),
            1, TurnSteps.DRAW, true, 0, 0);

        Observer o = new TestObserver();
        State d = o.finish(finishedGame);
        assertNull(d);
    }

    @Test
    void testToString() {
        Observer o = new TestObserver();

        assertEquals("TestObserver", o.toString());
    }


    private class TestObserver extends Observer {

    }

}