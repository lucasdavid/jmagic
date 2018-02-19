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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IsTurnOfActivePlayerTest {
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

        ValidationRule r = new IsTurnOfActivePlayer();
        r.onValidate(state);
        assertTrue(r.errors().isEmpty());
    }

    @Test
    void onValidate1() {
        State state = new State(start.playerStates(), start.turn,
            start.step.next(), start.done,
            0, 1,
            new AdvanceGame(), start);

        ValidationRule r = new IsTurnOfActivePlayer();
        r.onValidate(state);
        assertFalse(r.errors().isEmpty());
    }

    @Test
    void onValidate2() {
        State state = new State(start.playerStates(), start.turn,
            start.step.next(), start.done,
           1, 1,
            new AdvanceGame(), start);

        ValidationRule r = new IsTurnOfActivePlayer();
        r.onValidate(state);
        assertTrue(r.errors().isEmpty());
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
