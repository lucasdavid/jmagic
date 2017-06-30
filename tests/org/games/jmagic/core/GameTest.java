package org.games.jmagic.core;

import org.games.jmagic.actions.Action;
import org.games.jmagic.core.cards.Cards;
import org.games.jmagic.observers.LooseIfDrawingFromEmptyDeck;
import org.games.jmagic.observers.Observer;
import org.games.jmagic.observers.PassOrFinishIfLost;
import org.games.jmagic.observers.WinIfLastPlayerAlive;
import org.games.jmagic.core.states.State;
import org.games.jmagic.players.Player;
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
