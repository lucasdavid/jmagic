package org.jmagic.actions.rules.players.active;

import org.jmagic.actions.Action;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.Creature;
import org.jmagic.core.cards.ICard;
import org.jmagic.core.cards.lands.BasicLands;
import org.jmagic.core.cards.lands.Land;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HasCardsInFieldTest {
    private State initialState, advancedState;
    private List<Player> players;
    private List<ICard> cards;

    @BeforeEach
    void beforeEach() {
        cards = List.of(
            new Creature("creature-in-hand", 2, 4, List.of(BasicLands.FOREST)),
            new Land(BasicLands.MOUNTAIN)
        );

        players = List.of(
            new TestPlayer("test-player-1"),
            new TestPlayer("test-player-2"));

        initialState = new State(players, List.of(
            new Cards(
                new Creature("test-creature-1", 2, 3, List.of(BasicLands.FOREST)),
                new Creature("test-creature-2", 1, 1, List.of(BasicLands.MOUNTAIN))
            ),
            new Cards(
                new Creature("test-creature-3", 2, 3, List.of(BasicLands.SWAMP)),
                new Creature("test-creature-4", 1, 1, List.of(BasicLands.SWAMP))
            )),
            20);

        advancedState = new State(List.of(
            new State.PlayerState(players.get(0), 20, 20,
                Cards.EMPTY, Cards.EMPTY,
                new Cards(
                    cards.get(0),
                    cards.get(1),
                    new Creature("test-creature-1", 2, 3, List.of(BasicLands.FOREST)),
                    new Creature("test-creature-2", 1, 1, List.of(BasicLands.MOUNTAIN))),
                Cards.EMPTY,
                Collections.emptyMap(), Collections.emptyMap(), true),
            new State.PlayerState(players.get(1), 20, 20,
                Cards.EMPTY, Cards.EMPTY,
                new Cards(
                    new Creature("test-creature-2", 2, 3, List.of(BasicLands.FOREST)),
                    new Creature("test-creature-3", 1, 1, List.of(BasicLands.MOUNTAIN))),
                Cards.EMPTY,
                Collections.emptyMap(), Collections.emptyMap(), true)),
            42, TurnSteps.COMBAT_DAMAGE, false,
            0, 0);
    }

    @Test
    void validate() {
        ValidationRule r = new HasCardsInField(cards);
        r.validate(initialState);
        assertFalse(r.isValid());
    }

    @Test
    void validate1() {
        ValidationRule r = new HasCardsInField(cards);
        r.validate(advancedState);
        assertTrue(r.isValid());
    }

    @Test
    void validate2() {
        ValidationRule r = new HasCardsInField(cards.get(0), cards.get(1));
        r.validate(advancedState);
        assertTrue(r.isValid());
    }

    private static class TestPlayer extends Player {

        TestPlayer(String name) {
            super(UUID.randomUUID(), name);
        }

        @Override
        public Action act(State state) {
            return null;
        }
    }

}
