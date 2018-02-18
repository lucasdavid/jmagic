package org.jmagic.actions;

import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.actions.validation.rules.game.TurnIs;
import org.jmagic.actions.validation.rules.game.TurnStepIs;
import org.jmagic.actions.validation.rules.players.CardsDrawnCountReflectsMulliganCount;
import org.jmagic.actions.validation.rules.players.HasNotAlreadyInitiallyDrawnMoreThan;
import org.jmagic.actions.validation.rules.players.active.HasNotAlreadyDrawnInThisTurn;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.ICard;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;

import java.util.Collections;
import java.util.List;

import static org.jmagic.infrastructure.validation.rules.BasicRules.And;

/**
 * Initial Draw Action.
 * <p>
 * The active player draws {@code n} cards from their deck. If this action has
 * already been performed and this is a Paris Mulligan, then shuffle the hand
 * previously obtained back into the deck before drawing.
 *
 * @author ldavid
 */
public final class InitialDrawAction extends Action {

    private final int n;

    public InitialDrawAction() {
        this(7);
    }

    public InitialDrawAction(int n) {
        this.n = n;
    }

    @Override
    public State update(State state) {
        State.PlayerState p = state.activePlayerState();

        List<ICard> hand = p.hand.cards();
        List<ICard> deck = p.deck.cards();

        deck.addAll(hand);
        hand.clear();
        Collections.shuffle(deck);

        for (int i = 0; i < n; i++) hand.add(deck.remove(0));

        List<State.PlayerState> ps = state.playerStates();
        ps.set(ps.indexOf(p), new State.PlayerState(p.player, p.life(), p.originalLife(),
            new Cards(deck), new Cards(hand), p.field, p.graveyard,
            p.attackers, p.blockers, p.playing));


        return new State(ps, state.turn, state.step, state.done,
            state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    public ValidationRule validationRules() {
        return And(
            new TurnIs(0),
            new TurnStepIs(TurnSteps.INITIAL_DRAWING),
            new HasNotAlreadyInitiallyDrawnMoreThan(7),
            new HasNotAlreadyDrawnInThisTurn(),
            new CardsDrawnCountReflectsMulliganCount(n));
    }
}
