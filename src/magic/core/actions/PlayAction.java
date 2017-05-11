package magic.core.actions;

import magic.infrastructure.validation.rules.ValidationRule;
import magic.core.actions.validation.rules.game.TurnsStepIs;
import magic.core.actions.validation.rules.cards.CardHasAbility;
import magic.core.actions.validation.rules.cards.CardIsOfType;
import magic.core.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import magic.core.actions.validation.rules.players.active.HasCardInHand;
import magic.core.actions.validation.rules.players.active.HasLandsToPlayIt;
import magic.core.actions.validation.rules.players.active.HasNotPlayedALandThisTurn;
import magic.core.cards.Cards;
import magic.core.cards.ICard;
import magic.core.cards.Properties;
import magic.core.cards.lands.BasicLands;
import magic.core.cards.lands.Land;
import magic.core.states.State;
import magic.core.states.TurnStep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static magic.infrastructure.validation.connectives.Connectives.And;
import static magic.infrastructure.validation.connectives.Connectives.Not;
import static magic.infrastructure.validation.connectives.Connectives.Or;

/**
 * Play Action.
 * <p>
 * Play a card from the player's hand.
 *
 * @author ldavid
 */
public class PlayAction extends Action {

    private ICard card;

    public PlayAction(ICard card) {
        this.card = card;
    }

    @Override
    public State update(State state) {
        List<State.PlayerState> playerStates = state.playerStates();
        State.PlayerState p = playerStates.remove(state.turnsPlayerIndex);
        List<ICard> hand = p.hand.cards();
        List<ICard> field = p.field.cards();

        card = hand.remove(hand.indexOf(card));
        field.add(card);

        Collection<BasicLands> cost = card.cost();
        Collection<ICard> landsUsed = new ArrayList<>();

        field.stream()
            .filter(c -> c instanceof Land)
            .map(c -> ((Land) c))
            .filter(c -> !c.tapped())
            .forEach(c -> {
                if (cost.remove(c.kind()))
                    landsUsed.add(c.tap());
            });

        field.removeAll(landsUsed);
        field.addAll(landsUsed);

        p = new State.PlayerState(p.player, p.life(), p.maxLife(),
            p.deck, new Cards(hand), new Cards(field), p.graveyard);
        playerStates.add(state.turnsPlayerIndex, p);

        return new State(playerStates, state.turn, state.step, state.done,
            state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    public ValidationRule validationRules() {
        return And(
            new HasCardInHand(card),
            new HasLandsToPlayIt(card),
            Or(
                Not(new CardIsOfType(card, Land.class)),
                new HasNotPlayedALandThisTurn()),
            Or(
                And(
                    new ActiveAndTurnsPlayersAreTheSame(),
                    Or(
                        new TurnsStepIs(TurnStep.MAIN_1),
                        new TurnsStepIs(TurnStep.MAIN_2)
                    )
                ),
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
