package org.jmagic.players;

import org.jmagic.actions.*;
import org.jmagic.actions.validation.rules.players.HasPerformedThisTurn;
import org.jmagic.actions.validation.rules.players.active.HasNotAlreadyDrawnInThisTurn;
import org.jmagic.actions.validation.rules.players.active.HasNotAlreadyUntappedInThisTurn;
import org.jmagic.actions.validation.rules.players.active.HasNotPlayedALandThisTurn;
import org.jmagic.core.cards.ICard;
import org.jmagic.core.cards.lands.Land;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.experts.IExpert;

import java.util.Optional;

/**
 * Basic Magic Player.
 * <p>
 * Implementation of a player that performs basic actions, such as drawing,
 * untapping and discarding.
 *
 * @author ldavid
 */
public class BasicPlayer extends Player {

    private final IExpert mulliganExpert;

    public BasicPlayer(String name, IExpert mulliganExpert) {
        super(name);
        this.mulliganExpert = mulliganExpert;
    }

    @Override
    public Action act(State state) {
        State.PlayerState myState = state.playerState(this);

        if (state.step == TurnSteps.INITIAL_DRAWING) {
            final long cardsCount = myState.hand.size();
            final long landsCount = myState.hand.cards().stream()
                    .filter(c -> c instanceof Land)
                    .count();

            // I either haven't drawn yet or I've got a bad initial hand -- not (or too) many lands.
            // The good thing is that I can draw again.
            if ((cardsCount == 0 || landsCount < 2 || cardsCount - landsCount < 2)
                    && new InitialDrawAction().isValid(state)) {

                // Only draw `7 - mulliganCount`, as the game rules state.
                int mulliganCount = mulliganExpert.count(state, this);
                return new InitialDrawAction(7 - mulliganCount);
            }
        }

        if (state.activePlayerIndex != state.turnsPlayerIndex) {
            // BasicPlayer doesn't intercept other people's turns.
            return new AdvanceGameAction();
        }

        if (state.step == TurnSteps.DRAW && new HasNotAlreadyDrawnInThisTurn().isValid(state)) {
            return new DrawAction();
        }

        if (state.step == TurnSteps.UNTAP && new HasNotAlreadyUntappedInThisTurn().isValid(state)) {
            return new UntapAction(this);
        }

        if (state.step.isLast() && myState.hand.size() > 7) {
            // Needs to get rid of a card.
            return new DiscardAction(myState.hand.cards().get(0));
        }

        if (state.step == TurnSteps.MAIN_1 && new HasNotPlayedALandThisTurn().isValid(state)) {
            Optional<ICard> land = myState.hand.cards().stream()
                    .filter(c -> c instanceof Land)
                    .findFirst();

            if (land.isPresent()) {
                return new PlayAction(land.get());
            }
        }

        if (state.step == TurnSteps.COMBAT_DAMAGE
                && !new HasPerformedThisTurn(ComputeDamageAction.class).isValid(state)
                && !myState.attackers.isEmpty()) {
            return new ComputeDamageAction();
        }

        // Can't do anything else right now.
        return new AdvanceGameAction();
    }
}
