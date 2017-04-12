package magic.core.actions;

import magic.core.actions.validation.ValidationRule;
import magic.core.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import magic.core.actions.validation.rules.cards.CardHasAbility;
import magic.core.actions.validation.rules.cards.CardIsOfType;
import magic.core.actions.validation.rules.players.active.HasCardInHand;
import magic.core.actions.validation.rules.players.active.HasLandsToPlayIt;
import magic.core.actions.validation.rules.players.active.HasNotPlayedALandThisTurn;
import magic.core.actions.validation.rules.TurnsStepIs;
import magic.core.cards.Cards;
import magic.core.cards.ICard;
import magic.core.cards.Properties;
import magic.core.cards.lands.Land;
import magic.core.states.State;
import magic.core.states.TurnStep;

import java.util.List;
import java.util.stream.Collectors;

import static magic.core.actions.validation.ValidationRules.And;
import static magic.core.actions.validation.ValidationRules.Not;
import static magic.core.actions.validation.ValidationRules.Or;

/**
 * Play Action.
 * <p>
 * Play a card from the player's hand.
 *
 * @author ldavid
 */
public class PlayAction extends Action {

    private final ICard card;

    public PlayAction(ICard card) {
        this.card = card;
    }

    @Override
    public State update(State state) {
        List<State.PlayerState> playerStates = state.playerStates();
        State.PlayerState p = playerStates.remove(state.turnsPlayerIndex);
        List<ICard> hand = p.hand.cards();
        List<ICard> field = p.field.cards();

        ICard validCard = hand.remove(hand.indexOf(card));
        field.add(validCard);

        List<ICard> landsToUse = field.stream()
            .filter(c -> c instanceof Land && !((Land) c).used())
            .map(c -> (Land) c)
            .map(l -> new Land(l.id(), l.kind(), true))
            .collect(Collectors.toList());

        field.removeAll(landsToUse);
        field.addAll(landsToUse);

        p = new State.PlayerState(p.player, p.life(), p.maxLife(),
            p.deck, new Cards(hand), new Cards(field), p.graveyard);
        playerStates.add(state.turnsPlayerIndex, p);

        return new State(playerStates, state.turn, state.step, state.done,
            state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    protected ValidationRule validationRules() {
        return And(
            new ActiveAndTurnsPlayersAreTheSame(),
            new HasCardInHand(card),
            new HasLandsToPlayIt(card),
            Or(
                Not(new CardIsOfType(card, Land.class)),
                new HasNotPlayedALandThisTurn()),
            Or(
                new TurnsStepIs(TurnStep.MAIN_1),
                new TurnsStepIs(TurnStep.MAIN_2),
                new CardHasAbility(card, Properties.FLASH)
            ));
    }

    public ICard card() {
        return card;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", super.toString(), card.toString(true));
    }
}
