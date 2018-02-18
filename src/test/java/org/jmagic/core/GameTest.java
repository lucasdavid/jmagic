package org.jmagic.core;

import org.jmagic.actions.*;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.DeckBuilder;
import org.jmagic.core.states.State;
import org.jmagic.observers.*;
import org.jmagic.observers.Observer;
import org.jmagic.players.NaivePlayer;
import org.jmagic.players.Player;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        players = List.of(new TestPlayer("player-1"), new TestPlayer("player-2"));
        game = new Game(players,
            List.of(new Cards(), new Cards()), 20, 1000,
            List.of(new LooseIfDrawingFromEmptyDeck()));
    }


    @Test
    void testToString() {
        assertEquals("Game's players: [player-1, player-2]", game.toString());
    }

    @Test
    void startedAt() {
        assertNull(game.startedAt());
    }

    @Test
    void finishedAt() {
        assertNull(game.finishedAt());
    }

    @Test
    void running() {
        assertFalse(game.running());
    }

    @Test
    void players() {
        assertEquals(players, game.players());
    }

    @Test
    void winners() {
        assertNull(game.winners());
    }

    @Test
    void observers() {
        assertEquals("[LooseIfDrawingFromEmptyDeck, LooseOnNullAction, LooseOnInvalidActionAttempt, PassOrFinishIfLost, FinishIfLastPlayersAlive]",
            game.observers().toString());
    }

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
            20,
            1000,
            List.of(
                new LooseIfDrawingFromEmptyDeck()
            ));

        assertNotNull(game);
    }

    /**
     * Assert that Game always have `PassOrFinishIfLost` and
     * `FinishIfLastPlayersAlive` instances as testObservers (one of each).
     */
    @Test
    void testObservers() {
        Collection<Game> games = Set.of(
            new Game(
                List.of(new TestPlayer("player-1"), new TestPlayer("player-2")),
                List.of(new Cards(), new Cards()), 20, 1000,
                List.of(new LooseIfDrawingFromEmptyDeck())),
            new Game(
                List.of(new TestPlayer("player-1"), new TestPlayer("player-2")),
                List.of(new Cards(), new Cards()), 20, 1000,
                Collections.emptyList()),
            new Game(
                List.of(new TestPlayer("player-1"), new TestPlayer("player-2")),
                List.of(new Cards(), new Cards()), 20, 1000,
                List.of(new LooseIfDrawingFromEmptyDeck(), new FinishIfLastPlayersAlive(3))),
            new Game(
                List.of(new TestPlayer("player-1"), new TestPlayer("player-2")),
                List.of(new Cards(), new Cards()), 20, 1000,
                List.of(new LooseIfDrawingFromEmptyDeck(), new PassOrFinishIfLost(), new FinishIfLastPlayersAlive(3)))
        );

        for (Game g : games) {
            List<Observer> os = g.observers();
            assertEquals(1, os.stream().filter(o -> o instanceof PassOrFinishIfLost).count());
            assertEquals(1, os.stream().filter(o -> o instanceof FinishIfLastPlayersAlive).count());
        }
    }

    @Ignore
    void testRunStress() {
        Random r = new Random();
        DeckBuilder b = new DeckBuilder(20, r);

        new Game(
            List.of(new NaivePlayer("player-1"),
                new NaivePlayer("player-2")),
            List.of(b.random(),
                b.random()),
            20, 1000,
            List.of(new LooseIfDrawingFromEmptyDeck(),
                new LooseOnNullAction(),
                new LooseOnActTimeout(1000),
                new LooseOnInvalidActionAttempt(),
                new LooseOnIllegalActionAttempt(
                    DiscardAction.class,
                    DrawAction.class,
                    AdvanceGameAction.class,
                    PlayAction.class,
                    AttachAction.class,
                    UntapAction.class,
                    InitialDrawAction.class,
                    DeclareAttackersAction.class,
                    DeclareBlockersAction.class,
                    ComputeDamageAction.class),
                new PassOrFinishIfLost(),
                new FinishIfLastPlayersAlive()))
            .run();
    }

    class TestPlayer extends Player {

        TestPlayer(String name) {
            super(name);
        }

        @Override
        public Action act(State state) {
            return null;
        }
    }

}
