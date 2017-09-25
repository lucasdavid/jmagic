package org.jmagic.actions;

import org.jmagic.actions.validation.rules.game.TurnsStepIs;
import org.jmagic.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import org.jmagic.actions.validation.rules.players.active.HasCardsInTheirDeck;
import org.jmagic.actions.validation.rules.players.active.HasNotAlreadyDrawnInThisTurn;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.ICard;
import org.jmagic.core.states.State;
import org.jmagic.core.states.State.PlayerState;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.Player;

import java.util.List;
import java.util.Objects;

import static org.jmagic.infrastructure.validation.basic.Connectives.And;

/**
 * Draw Action.
 * <p>
 * Player draws a card from their deck.
 *
 * @author ldavid
 */
public final class DrawAction extends Action {

    private final Player player;

    public DrawAction() {
        this(null);
    }

    public DrawAction(Player player) {
        this.player = player;
    }

    @Override
    public State update(State state) {
        List<PlayerState> ps = state.playerStates();
        State.PlayerState p = player != null
                ? state.playerState(player)
                : state.activePlayerState();

        List<ICard> hand = p.hand.cards();
        List<ICard> deck = p.deck.cards();

        hand.add(deck.remove(0));

        ps.set(ps.indexOf(p),
                new State.PlayerState(p.player, p.life(), p.maxLife(),
                        new Cards(deck), new Cards(hand), p.field, p.graveyard,
                        p.attackers, p.blockers, p.playing));

        return new State(ps, state.turn, state.step, state.done,
                state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    public boolean equals(Object o) {
        try {
            return this == o || player == ((DrawAction) o).player || player.equals(((DrawAction) o).player);
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 53;
        hash = 7 * hash + Objects.hashCode(player);
        return hash;
    }

    @Override
    public ValidationRule validationRules() {
        return And(
                new HasCardsInTheirDeck(),
                new TurnsStepIs(TurnSteps.DRAW),
                new ActiveAndTurnsPlayersAreTheSame(),
                new HasNotAlreadyDrawnInThisTurn());
    }
}
