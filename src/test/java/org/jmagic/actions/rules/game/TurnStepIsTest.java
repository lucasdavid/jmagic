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

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurnStepIsTest {
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
        ValidationRule r = new TurnStepIs(TurnSteps.COMBAT_DAMAGE);
        r.onValidate(state);
        assertTrue(r.errors().isEmpty(), () -> r.errors().toString());
    }

    @Test
    void onValidate1() {
        ValidationRule r = new TurnStepIs(TurnSteps.DRAW);
        r.onValidate(state);
        assertFalse(r.errors().isEmpty());
    }

    @Test
    void _toString() {
        assertEquals("TurnStepIs(COMBAT_DAMAGE)", new TurnStepIs(TurnSteps.COMBAT_DAMAGE).toString());
    }

}