package org.games.jmagic.core;

import org.games.jmagic.core.cards.lands.BasicLands;
import org.games.jmagic.observers.Observer;
import org.games.jmagic.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GameBuilder Test.
 *
 * @author ldavid
 */
class GameBuilderTest {

    List<Player> players;
    List<Set<BasicLands>> deckColors;
    List<Observer> observers;
    int numberOfCards = 40;
    long actTimeout = 1000;
    Random r;

    @BeforeEach
    void setUp() {
        players = Collections.emptyList();
        deckColors = Collections.emptyList();
        observers = Collections.emptyList();
        r = new Random(2341);
    }

    @Test
    void build1() {
        GameBuilder gb = new GameBuilder(players, 3, actTimeout, observers, r);
        testGameConsistency(gb.build());
    }

    @Test
    void build2() {
        GameBuilder gb = new GameBuilder(players, deckColors, numberOfCards, actTimeout, observers, r);
        testGameConsistency(gb.build());
    }

    void testGameConsistency(Game game) {
        assertTrue(game.observers().containsAll(observers));
        assertNull(game.winners());
        assertNull(game.startedAt());
        assertNull(game.finishedAt());

        // Todo: this class is strongly coupled to many other implementations
        // in this code, which branches out the cases that must be tested.
        // It's somewhat understandable as it's purpose is to assist users to
        // create random games, but this test must cover many more cases.
    }
}
