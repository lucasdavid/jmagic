package org.jmagic.experts;

import org.jmagic.actions.AdvanceGame;
import org.jmagic.actions.InitialDraw;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.DeckBuilder;
import org.jmagic.core.states.State;
import org.jmagic.players.Player;
import org.jmagic.players.RandomPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

class MulliganExpertTest {
    private State first, second, third;

    @BeforeEach
    void setUp() {
        Random r = new Random(42);

        List<Player> players = List.of(
                new RandomPlayer("test-player-1"),
                new RandomPlayer("test-player-2"));

        DeckBuilder b = new DeckBuilder(40, r);
        List<Cards> cards = List.of(b.random(), b.random());

        this.first = new AdvanceGame().update(
                new AdvanceGame().update(
                        new AdvanceGame().update(
                                new AdvanceGame().update(new State(players, cards, 20)))));
        this.second = new InitialDraw().update(first);
        this.third = new InitialDraw().update(second);
    }

    @Test
    void count() {
        IExpert expert = new MulliganExpert();

        assertEquals(0, expert.count(first, first.activePlayerState().player));
        assertEquals(1, expert.count(second, second.activePlayerState().player));
        assertEquals(2, expert.count(third, third.activePlayerState().player));
    }

}