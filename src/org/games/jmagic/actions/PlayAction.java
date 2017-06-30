package org.games.jmagic.actions;

import org.games.jmagic.infrastructure.validation.rules.ValidationRule;
import org.games.jmagic.actions.validation.rules.game.TurnsStepIs;
import org.games.jmagic.actions.validation.rules.cards.CardHasAbility;
import org.games.jmagic.actions.validation.rules.cards.CardIsOfType;
import org.games.jmagic.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import org.games.jmagic.actions.validation.rules.players.active.HasCardInHand;
import org.games.jmagic.actions.validation.rules.players.active.HasLandsToPlayIt;
import org.games.jmagic.actions.validation.rules.players.active.HasNotPlayedALandThisTurn;
import org.games.jmagic.core.cards.Cards;
import org.games.jmagic.core.cards.ICard;
import org.games.jmagic.core.cards.Properties;
import org.games.jmagic.core.cards.lands.BasicLands;
import org.games.jmagic.core.cards.lands.Land;
import org.games.jmagic.core.states.State;
import org.games.jmagic.core.states.TurnSteps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.games.jmagic.infrastructure.validation.basic.Connectives.*;

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
                    new ActiveAndTurnsPlayersAreTheSame(),
                    Or(
                        new TurnsStepIs(TurnSteps.MAIN_1),
                        new TurnsStepIs(TurnSteps.MAIN_2)
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
            return this == o || card.equals(((PlayAction) o).card);
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
        return String.format("%s: %s", super.toString(), card.toString(true));
    }
}