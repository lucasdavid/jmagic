package org.jmagic.actions.validation.rules.players;

import org.jmagic.actions.Action;
import org.jmagic.actions.AdvanceGameAction;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.Creature;
import org.jmagic.core.cards.lands.BasicLands;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HasPerformedThisTurnTest {

    private State initialState, advancedState;
    private List<Player> players;

    @BeforeEach
    void beforeEach() {
        players = List.of(
            new TestPlayer("test-player-1"),
            new TestPlayer("test-player-2"));

        initialState = new State(players, List.of(
            new Cards(List.of(
                new Creature("test-creature-1", 2, 3, List.of(BasicLands.FOREST)),
                new Creature("test-creature-2", 1, 1, List.of(BasicLands.MOUNTAIN))
            )),
            new Cards(List.of(
                new Creature("test-creature-3", 2, 3, List.of(BasicLands.SWAMP)),
                new Creature("test-creature-4", 1, 1, List.of(BasicLands.SWAMP))
            ))),
            20);

        advancedState = new State(initialState.playerStates(), initialState.turn,
            initialState.step.next(), initialState.done,
            initialState.turnsPlayerIndex, initialState.activePlayerIndex,
            new AdvanceGameAction(), initialState);
    }

    @Test
    void testRule() {
        ValidationRule rule;

        // current active player.
        rule = new HasPerformedThisTurn(AdvanceGameAction.class);
        assertTrue(rule.isValid(advancedState));
        assertFalse(rule.isValid(initialState));

        // active player should also be the first player in the list.
        rule = new HasPerformedThisTurn(AdvanceGameAction.class, players.get(0));
        assertTrue(rule.isValid(advancedState));
        assertFalse(rule.isValid(initialState));


        // This player should have not performed any actions so far.
        rule = new HasPerformedThisTurn(AdvanceGameAction.class, players.get(1));
        assertFalse(rule.isValid(advancedState));
        assertFalse(rule.isValid(initialState));

        // Nor this new player.
        Player p = new TestPlayer("test-player-3");
        assertThrows(NoSuchElementException.class,
            () -> new HasPerformedThisTurn(AdvanceGameAction.class, p).isValid(initialState));
        assertThrows(NoSuchElementException.class,
            () -> new HasPerformedThisTurn(AdvanceGameAction.class, p).isValid(advancedState));
    }

    @Test
    void testToString() {
        ValidationRule r;

        r = new HasPerformedThisTurn(AdvanceGameAction.class);
        assertEquals("HasPerformedThisTurn(AdvanceGameAction, active)", r.toString());

        r = new HasPerformedThisTurn(AdvanceGameAction.class, players.get(0));
        assertEquals("HasPerformedThisTurn(AdvanceGameAction, test-player-1)", r.toString());
    }


    private class TestPlayer extends Player {

        public TestPlayer(String name) {
            super(UUID.randomUUID(), name);
        }

        @Override
        public Action act(State state) {
            return null;
        }
    }

}