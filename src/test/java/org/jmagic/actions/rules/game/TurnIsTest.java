package org.jmagic.actions.rules.game;

import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.Creature;
import org.jmagic.core.cards.lands.BasicLands;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.RandomPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static junit.framework.TestCase.*;

class TurnIsTest {
    private State state;

    @BeforeEach
    void setUp() {
        state = new State(List.of(
            new State.PlayerState(new RandomPlayer("test-player-1"),
                new Cards(
                    new Creature("test-creature-1", 2, 3, List.of(BasicLands.FOREST)),
                    new Creature("test-creature-2", 1, 1, List.of(BasicLands.MOUNTAIN))),
                20),
            new State.PlayerState(new RandomPlayer("test-player-2"),
                new Cards(
                    new Creature("test-creature-3", 2, 3, List.of(BasicLands.SWAMP)),
                    new Creature("test-creature-4", 1, 1, List.of(BasicLands.SWAMP))),
                20)
        ), 42, TurnSteps.COMBAT_DAMAGE, false,
            0, 0);
    }

    @Test
    void onValidate() {
        ValidationRule r = new TurnIs(42);
        r.onValidate(state);
        assertTrue(r.errors().isEmpty());
    }

    @Test
    void onValidate1() {
        ValidationRule r = new TurnIs(10);
        r.onValidate(state);
        assertFalse(r.errors().isEmpty());
    }

    @Test
    void _toString() {
        assertEquals("TurnIs(42)", new TurnIs(42).toString());
    }

}
