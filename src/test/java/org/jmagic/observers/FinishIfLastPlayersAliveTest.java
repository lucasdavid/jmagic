package org.jmagic.observers;

import org.jmagic.actions.FinishGame;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.players.RandomPlayer;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

class FinishIfLastPlayersAliveTest {
    @Test
    void beforePlayerAct() {
        State s = new State(
            List.of(
                new State.PlayerState(new RandomPlayer("test-1"), 10, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, true),
                new State.PlayerState(new RandomPlayer("test-2"), 20, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, false)),
            1, TurnSteps.DRAW, false, 1, 1);

        Observer o = new FinishIfLastPlayersAlive();
        State actual = o.beforePlayerAct(s);
        assertNotEquals(s, actual);
        assertTrue(actual.actionThatLedToThisState instanceof FinishGame);
        assertTrue(actual.done);
    }

    @Test
    void beforePlayerAct1() {
        State s = new State(
            List.of(
                new State.PlayerState(new RandomPlayer("test-1"), 1, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, true),
                new State.PlayerState(new RandomPlayer("test-2"), 20, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, true)),
            1, TurnSteps.DRAW, false, 1, 1);

        Observer o = new FinishIfLastPlayersAlive();
        State actual = o.beforePlayerAct(s);
        assertEquals(s, actual);
    }

    @Test
    void afterPlayerAct() {
        State s = new State(
            List.of(
                new State.PlayerState(new RandomPlayer("test-1"), -2, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, true),
                new State.PlayerState(new RandomPlayer("test-2"), 20, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, true)),
            1, TurnSteps.DRAW, false, 1, 1);

        Observer o = new FinishIfLastPlayersAlive();
        State actual = o.afterPlayerAct(s, null, 0, 0);
        assertNotEquals(s, actual);
        assertTrue(actual.actionThatLedToThisState instanceof FinishGame);
        assertTrue(actual.done);
    }

}