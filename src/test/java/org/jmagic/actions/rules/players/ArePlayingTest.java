package org.jmagic.actions.rules.players;

import org.jmagic.actions.Action;
import org.jmagic.actions.AdvanceGame;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.Creature;
import org.jmagic.core.cards.lands.BasicLands;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArePlayingTest {

    private List<Player> players;
    private State start;

    @BeforeEach
    void setUp() {
        players = List.of(
            new TestPlayer("test-player-1"),
            new TestPlayer("test-player-2"));

        start = new State(players, List.of(
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
    void onValidate() {
        State state = new State(start.playerStates(), start.turn,
            start.step.next(), start.done,
            0, 0,
            new AdvanceGame(), start);

        ValidationRule r = new ArePlaying(players.get(0));
        r.onValidate(state);
        assertTrue(r.errors().isEmpty());
    }

    @Test
    void onValidate1() {
        State state = new State(start.playerStates(), start.turn,
            start.step.next(), start.done,
            0, 0,
            new AdvanceGame(), start);

        ValidationRule r = new ArePlaying(players);
        r.onValidate(state);
        assertTrue(r.errors().isEmpty());
    }

    @Test
    void onValidate2() {
        List<State.PlayerState> ps = start.playerStates();
        State.PlayerState p1 = ps.get(1);

        ps = List.of(ps.get(0), new State.PlayerState(
            p1.player, p1.life(), p1.originalLife(), p1.deck, p1.hand, p1.field,
            p1.graveyard, Collections.emptyMap(), Collections.emptyMap(), false));
        State state = new State(ps, start.turn,
            start.step.next(), start.done,
            0, 0,
            new AdvanceGame(), start);

        ValidationRule r = new ArePlaying(players);
        r.onValidate(state);
        assertFalse(r.errors().isEmpty());
        assertEquals("[test-player-2 isn\'t playing]", r.errors().toString());
    }


    private class TestPlayer extends Player {

        public TestPlayer(String name) {
            super(name);
        }

        @Override
        public Action act(State state) {
            return null;
        }
    }

}
