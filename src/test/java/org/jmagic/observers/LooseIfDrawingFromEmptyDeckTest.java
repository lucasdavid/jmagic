package org.jmagic.observers;

import org.jmagic.actions.Disqualify;
import org.jmagic.actions.Draw;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.Creature;
import org.jmagic.core.cards.lands.BasicLands;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.players.RandomPlayer;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LooseIfDrawingFromEmptyDeckTest {

    @Test
    void afterPlayerAct() {
        State drawing = new State(
            List.of(
                new State.PlayerState(new RandomPlayer("test-1"), 20, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, true),
                new State.PlayerState(new RandomPlayer("test-2"), 20, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, true)),
            1, TurnSteps.DRAW, false, 0, 0);

        Observer o = new LooseIfDrawingFromEmptyDeck();

        State actual = o.afterPlayerAct(drawing, new Draw(), 0, 100);

        assertTrue(actual.actionThatLedToThisState instanceof Disqualify);
        assertFalse(actual.playerStates().get(0).playing);
        assertTrue(actual.playerStates().get(1).playing);
    }

    @Test
    void afterPlayerAct1() {
        State drawing = new State(
            List.of(
                new State.PlayerState(new RandomPlayer("test-1"), 20, 20,
                    new Cards(new Creature("test-creature-1", 10, 2, List.of(BasicLands.FOREST))),
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, true),
                new State.PlayerState(new RandomPlayer("test-2"), 20, 20,
                    Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
                    Collections.EMPTY_MAP, Collections.EMPTY_MAP, true)),
            1, TurnSteps.DRAW, false, 0, 0);

        Observer o = new LooseIfDrawingFromEmptyDeck();

        State actual = o.afterPlayerAct(drawing, new Draw(), 0, 100);

        assertSame(drawing, actual);
        assertTrue(actual.playerStates().get(0).playing);
        assertTrue(actual.playerStates().get(1).playing);
    }

}
