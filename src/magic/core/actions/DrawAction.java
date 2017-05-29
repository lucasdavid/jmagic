package magic.core.actions;

import magic.core.Player;
import magic.infrastructure.validation.rules.ValidationRule;
import magic.core.actions.validation.rules.game.TurnsStepIs;
import magic.core.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import magic.core.actions.validation.rules.players.active.HasCardsInTheirDeck;
import magic.core.actions.validation.rules.players.active.HasNotAlreadyDrawnInThisTurn;
import magic.core.cards.Cards;
import magic.core.cards.ICard;
import magic.core.states.State;
import magic.core.states.State.PlayerState;
import magic.core.states.TurnSteps;

import java.util.List;

import static magic.infrastructure.validation.basic.Connectives.And;

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
    public ValidationRule validationRules() {
        return And(
            new HasCardsInTheirDeck(),
            new TurnsStepIs(TurnSteps.DRAW),
            new ActiveAndTurnsPlayersAreTheSame(),
            new HasNotAlreadyDrawnInThisTurn());
    }
}
