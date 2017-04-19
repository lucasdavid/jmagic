package magic.core.actions;

import magic.core.actions.validation.ValidationRule;
import magic.core.actions.validation.rules.IsOnNthTurn;
import magic.core.actions.validation.rules.TurnsStepIs;
import magic.core.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import magic.core.cards.Cards;
import magic.core.cards.ICard;
import magic.core.states.State;
import magic.core.states.State.PlayerState;
import magic.core.states.TurnStep;

import java.util.List;
import java.util.stream.Collectors;

import static magic.core.actions.validation.ValidationRules.And;

/**
 * Initial Draw Action.
 * <p>
 * All players draw {@code n} cards from their decks.
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
        List<PlayerState> playersInfo = state.playerStates().stream()
            .map(p -> {
                List<ICard> hand = p.hand.cards();
                List<ICard> deck = p.deck.cards();

                for (int i = 0; i < n; i++) hand.add(deck.remove(0));

                return new PlayerState(p.player, p.life(), p.maxLife(),
                    new Cards(deck), new Cards(hand), p.field, p.graveyard);
            })
            .collect(Collectors.toList());

        return new State(playersInfo, state.turn, state.step, state.done,
            state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    protected ValidationRule validationRules() {
        return And(
            new IsOnNthTurn(0),
            new TurnsStepIs(TurnStep.UNTAP),
            new ActiveAndTurnsPlayersAreTheSame());
    }
}
