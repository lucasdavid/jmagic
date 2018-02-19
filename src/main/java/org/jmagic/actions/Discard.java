package org.jmagic.actions;

import org.jmagic.actions.rules.game.TurnStepIs;
import org.jmagic.actions.rules.players.IsTurnOfActivePlayer;
import org.jmagic.actions.rules.players.active.HasFewerCardsInHandThan;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.ICard;
import org.jmagic.core.states.State;
import org.jmagic.core.states.State.PlayerState;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.infrastructure.validation.rules.ValidationRule;

import java.util.List;
import java.util.Objects;

import static org.jmagic.infrastructure.validation.rules.BasicRules.And;
import static org.jmagic.infrastructure.validation.rules.BasicRules.Not;

/**
 * Discard Action.
 * <p>
 * Current player discards a card from their hand.
 *
 * @author ldavid
 */
public final class Discard extends Action {

    private final ICard card;

    public Discard(ICard card) {
        this.card = card;
    }

    @Override
    public State update(State state) {
        List<PlayerState> playersInfo = state.playerStates();
        PlayerState p = playersInfo.remove(state.turnsPlayerIndex);

        List<ICard> hand = p.hand.cards();
        List<ICard> graveyard = p.graveyard.cards();

        graveyard.add(hand.remove(hand.indexOf(card)));

        playersInfo.add(state.turnsPlayerIndex,
            new PlayerState(p.player, p.life(), p.originalLife(),
                p.deck, new Cards(hand), p.field, new Cards(graveyard),
                p.attackers, p.blockers, p.playing));

        return new State(playersInfo, state.turn, state.step, state.done,
            state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    public ValidationRule validationRules() {
        return And(
            new IsTurnOfActivePlayer(),
            new TurnStepIs(TurnSteps.CLEANUP),
            Not(new HasFewerCardsInHandThan(8)));
    }

    @Override
    public boolean equals(Object o) {
        try {
            return this == o || card.equals(((Discard) o).card);
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 73;
        hash = 5 * hash + Objects.hashCode(card);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%s %s", super.toString(), card);
    }
}
