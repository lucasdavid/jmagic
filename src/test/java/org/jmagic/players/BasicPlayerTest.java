package org.jmagic.players;

import org.jmagic.actions.Action;
import org.jmagic.actions.AdvanceGame;
import org.jmagic.actions.ComputeDamage;
import org.jmagic.actions.Discard;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.Creature;
import org.jmagic.core.cards.lands.BasicLands;
import org.jmagic.core.cards.lands.Land;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.experts.IExpert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

class BasicPlayerTest {

    private BasicPlayer jane;

    @BeforeEach
    void setUp() {
        IExpert mulliganExpert = (state, player) -> 0;

        jane = new BasicPlayer("jane", mulliganExpert);
    }

    @Test
    void actDiscardIfNecessary() {
        // A state where the jane has 8 cards in her hand and it's the cleanup step.
        Cards hand = new Cards(
            new Land(BasicLands.SWAMP), new Land(BasicLands.SWAMP),
            new Land(BasicLands.SWAMP), new Land(BasicLands.SWAMP),
            new Land(BasicLands.SWAMP), new Land(BasicLands.SWAMP),
            new Land(BasicLands.SWAMP), new Land(BasicLands.SWAMP));

        List<State.PlayerState> playerStates = List.of(new State.PlayerState(jane, 20, 20,
            Cards.EMPTY, hand, Cards.EMPTY, Cards.EMPTY,
            Collections.emptyMap(), Collections.emptyMap(), true));

        State state = new State(playerStates, 0, TurnSteps.CLEANUP, false, 0, 0);

        Action action = jane.act(state);

        // Jane should necessarily discard.
        assertThat(action, instanceOf(Discard.class));
    }

    @Test
    void actComputeDamage() {
        // A state where the jane has attackers and it's the COMBAT_DAMAGE step.
        List<State.PlayerState> playerStates = List.of(new State.PlayerState(jane, 20, 20,
            Cards.EMPTY, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY,
            Map.of(
                new Creature("test-creature-1", 3, 2, null), jane,
                new Creature("test-creature-2", 12, 4, null), jane),
            Collections.emptyMap(), true));

        State state = new State(playerStates, 0, TurnSteps.COMBAT_DAMAGE,
            false, 0, 0);

        // Jane should necessarily compute the damage dealt this turn.
        Action action = jane.act(state);
        assertThat(action, instanceOf(ComputeDamage.class));

        // Let's say Jane computed the damage.
        state = new State(playerStates, 0, TurnSteps.COMBAT_DAMAGE,
            false, 0, 0,
            new ComputeDamage(), state);

        // Jane should necessarily advance the game.
        action = jane.act(state);
        assertThat(action, instanceOf(AdvanceGame.class));
    }

}
