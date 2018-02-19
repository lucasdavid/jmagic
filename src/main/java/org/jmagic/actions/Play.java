package org.jmagic.actions;

import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.actions.rules.game.TurnStepIs;
import org.jmagic.actions.rules.cards.CardHasAbility;
import org.jmagic.actions.rules.cards.CardIsOfType;
import org.jmagic.actions.rules.players.IsTurnOfActivePlayer;
import org.jmagic.actions.rules.players.active.HasCardInHand;
import org.jmagic.actions.rules.players.active.HasLandsToPlayIt;
import org.jmagic.actions.rules.players.active.HasNotPlayedALandThisTurn;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.cards.ICard;
import org.jmagic.core.cards.Properties;
import org.jmagic.core.cards.lands.BasicLands;
import org.jmagic.core.cards.lands.Land;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.jmagic.infrastructure.validation.rules.BasicRules.*;

/**
 * Play Action.
 * <p>
 * Play a card from the player's hand.
 *
 * @author ldavid
 */
public class Play extends Action {

    private ICard card;

    public Play(ICard card) {
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

        p = new State.PlayerState(p.player, p.life(), p.originalLife(),
            p.deck, new Cards(hand), new Cards(field), p.graveyard,
            p.attackers, p.blockers, p.playing);
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
                    new IsTurnOfActivePlayer(),
                    Or(
                        new TurnStepIs(TurnSteps.MAIN_1),
                        new TurnStepIs(TurnSteps.MAIN_2)
                    )
                ),
                new CardHasAbility(card, Properties.FLASH)
            ));
    }

    public ICard card() {
        return card;
    }

    @Override
    public boolean equals(Object o) {
        try {
            return this == o || card.equals(((Play) o).card);
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 101;
        hash = 17 * hash + Objects.hashCode(card);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%s %s", super.toString(), card.toString(true));
    }
}
