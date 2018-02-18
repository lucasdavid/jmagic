package org.jmagic.core.states;

import org.jmagic.actions.Action;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.Creature;
import org.jmagic.core.cards.lands.BasicLands;
import org.jmagic.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    private State state;
    private List<Player> players;

    private class TestPlayer extends Player {

        public TestPlayer(String name) {
            super(UUID.randomUUID(), name);
        }

        @Override
        public Action act(State state) {
            return null;
        }
    }

    @BeforeEach
    void beforeEach() {
        players = List.of(
            new TestPlayer("test-player-1"),
            new TestPlayer("test-player-2"));

        state = new State(players, List.of(
            new Cards(List.of(
                new Creature("test-creature-1", 2, 3, List.of(BasicLands.FOREST)),
                new Creature("test-creature-2", 1, 1, List.of(BasicLands.MOUNTAIN))
            )),
            new Cards(List.of(
                new Creature("test-creature-3", 2, 3, List.of(BasicLands.SWAMP)),
                new Creature("test-creature-4", 1, 1, List.of(BasicLands.SWAMP))
            ))),
            20);
    }

    @Test
    void turnsPlayerState() {
        assertEquals(state.turnsPlayerIndex,
            state.playerStates().indexOf(state.turnsPlayerState()));

        State s = new State(state.playerStates(), 1,
            TurnSteps.INITIAL_DRAWING,
            false,
            1, 1);

        assertEquals(s.turnsPlayerIndex, s.playerStates().indexOf(s.turnsPlayerState()));
    }

    @Test
    void activePlayerState() {
        assertEquals(state.activePlayerIndex,
            state.playerStates().indexOf(state.activePlayerState()));

        State s = new State(state.playerStates(), 1,
            TurnSteps.INITIAL_DRAWING,
            false,
            1, 1);

        assertEquals(s.activePlayerIndex, s.playerStates().indexOf(s.activePlayerState()));
    }

    @Test
    void playerState() {
        State.PlayerState ps = state.playerState(0);

        assertEquals(players.get(0), ps.player);
        assertEquals(20, ps.originalLife());
        assertEquals(20, ps.life());
        assertEquals(20, ps.effectiveLife());
        assertEquals(20, ps.effectiveOriginalLife());
    }

    @Test
    void playerState1() {
        for (Player p : players) {
            State.PlayerState ps = state.playerState(p);
            assertEquals(p, ps.player);
        }
    }

    @Test
    void playerState2() {
        Player unknownPlayer = new TestPlayer("test-player-3");
        assertThrows(NoSuchElementException.class,
            () -> state.playerState(unknownPlayer));
    }

    @Test
    void playerStates() {
        List<State.PlayerState> pss = state.playerStates();

        for (int i = 0; i < players.size(); i++) {
            assertEquals(players.get(i), pss.get(i).player);
        }
    }

    @Test
    void playerViewModel() {
        State pvm = this.state.playerViewModel(players.get(0));
        List<State.PlayerState> pss = pvm.playerStates();

        // Players should be aware of their own hands, but not their decks.
        State.PlayerState self = pss.get(0);
        assertNotNull(self.hand);
        assertNull(self.deck);

        // Players should not be aware of their opponents' hands or decks.
        State.PlayerState opponent = pss.get(1);
        assertNull(opponent.hand);
        assertNull(opponent.deck);
    }

    @Test
    void playerStateHashCode() {
        Set<State.PlayerState> playerStates = new HashSet<>(state.playerStates());

        // Old users are equal to one in the set.
        State.PlayerState ps = state.playerState(0);
        assertFalse(playerStates.add(ps));

        // New users are different from the previous ones.
        State.PlayerState newPs = new State.PlayerState(
            new TestPlayer("test-player-3"),
            null, 20);
        assertTrue(playerStates.add(newPs));
    }

    @Test
    void playerStateEquals() {
        List<State.PlayerState> pss = state.playerStates();

        Player p = new TestPlayer("test-player-3");

        assertEquals(new State.PlayerState(p, null, 20), new State.PlayerState(p, null, 15));
        assertNotEquals(pss.get(0), pss.get(1));
        assertNotEquals(pss.get(0), null);
        assertNotEquals(new State.PlayerState(null, null, 20), null);
    }

    @Test
    void playerTakeDamage() {
        State.PlayerState ps = state.activePlayerState();
        int damage = 5;

        assertEquals(20, ps.life());
        assertEquals(ps.originalLife(), ps.life());

        State.PlayerState updatedPs = ps.takeDamage(damage);
        assertEquals(20, ps.life());
        assertEquals(ps.originalLife(), ps.life());

        assertEquals(15, updatedPs.life());
        assertEquals(updatedPs.originalLife() - damage, updatedPs.life());
    }

    @Test
    void testToString() {
        assertTrue(state.toString().startsWith("turn: 0, INITIAL_DRAWING"));
    }
}
