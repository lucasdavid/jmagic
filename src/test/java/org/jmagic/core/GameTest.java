package org.jmagic.core;

import org.jmagic.actions.Action;
import org.jmagic.core.cards.Cards;
import org.jmagic.observers.LooseIfDrawingFromEmptyDeck;
import org.jmagic.observers.Observer;
import org.jmagic.observers.PassOrFinishIfLost;
import org.jmagic.observers.WinIfLastPlayerAlive;
import org.jmagic.core.states.State;
import org.jmagic.players.Player;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameTest {

    @Test
    void sanity() {
        Game game = new Game(
            List.of(
                new TestPlayer("player-1"),
                new TestPlayer("player-2")),
            List.of(
                new Cards(),
                new Cards()
            ),
            1000,
            List.of(
                new LooseIfDrawingFromEmptyDeck()
            ));

        assertNotNull(game);
    }

    /**
     * Assert that Game always have `PassOrFinishIfLost` and
     * `WinIfLastPlayerAlive` instances as observers (one of each).
     */
    @Test
    void observers() {
        Collection<Game> games = Set.of(
            new Game(
                List.of(new TestPlayer("player-1"), new TestPlayer("player-2")),
                List.of(new Cards(), new Cards()), 1000,
                List.of(new LooseIfDrawingFromEmptyDeck())),
            new Game(
                List.of(new TestPlayer("player-1"), new TestPlayer("player-2")),
                List.of(new Cards(), new Cards()), 1000,
                Collections.emptyList()),
            new Game(
                List.of(new TestPlayer("player-1"), new TestPlayer("player-2")),
                List.of(new Cards(), new Cards()), 1000,
                List.of(new LooseIfDrawingFromEmptyDeck(), new WinIfLastPlayerAlive(3))),
            new Game(
                List.of(new TestPlayer("player-1"), new TestPlayer("player-2")),
                List.of(new Cards(), new Cards()), 1000,
                List.of(new LooseIfDrawingFromEmptyDeck(), new PassOrFinishIfLost(), new WinIfLastPlayerAlive(3)))
        );

        for (Game g : games) {
            List<Observer> os = g.observers();
            assertEquals(1, os.stream().filter(o -> o instanceof PassOrFinishIfLost).count());
            assertEquals(1, os.stream().filter(o -> o instanceof WinIfLastPlayerAlive).count());
        }
    }

    class TestPlayer extends Player {

        public TestPlayer(String name) {
            super(name);
        }

        @Override
        public Action act(State state) {
            return null;
        }
    }

}
