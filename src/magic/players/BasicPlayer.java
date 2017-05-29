package magic.players;

import magic.core.Player;
import magic.core.actions.Action;
import magic.core.actions.AdvanceGameAction;
import magic.core.actions.ComputeDamageAction;
import magic.core.actions.DiscardAction;
import magic.core.actions.DrawAction;
import magic.core.actions.InitialDrawAction;
import magic.core.actions.PlayAction;
import magic.core.actions.UntapAction;
import magic.core.actions.validation.rules.players.HasPerformedThisTurn;
import magic.core.actions.validation.rules.players.active.HasNotAlreadyDrawnInThisTurn;
import magic.core.actions.validation.rules.players.active.HasNotAlreadyUntappedInThisTurn;
import magic.core.actions.validation.rules.players.active.HasNotPlayedALandThisTurn;
import magic.core.cards.lands.Land;
import magic.core.experts.IExpert;
import magic.core.states.State;
import magic.core.states.TurnSteps;

import java.util.Comparator;
import java.util.NoSuchElementException;

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

    public BasicPlayer(IExpert mulliganExpert) {
        super();
        this.mulliganExpert = mulliganExpert;
    }

    public BasicPlayer(String name, IExpert mulliganExpert) {
        super(name);
        this.mulliganExpert = mulliganExpert;
    }

    @Override
    public Action act(State state) {
        State.PlayerState myState = state.playerState(this);

        if (state.step == TurnSteps.DRAW && state.turn == 0) {
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

        if (!state.turnsPlayerState().player.equals(this)) {
            // Don't intercept other people's turns.
            return new AdvanceGameAction();
        }

        if (state.step == TurnSteps.DRAW && new HasNotAlreadyDrawnInThisTurn().isValid(state)) {
            return new DrawAction();
        }

        if (state.step == TurnSteps.UNTAP && new HasNotAlreadyUntappedInThisTurn().isValid(state)) {
            return new UntapAction(this);
        }

        if (state.step == TurnSteps.CLEANUP && myState.hand.size() > 7) {
            return new DiscardAction(myState.hand.cards().get(0));
        }

        if (state.step == TurnSteps.MAIN_1 && new HasNotPlayedALandThisTurn().isValid(state)) {
            try {
                return new PlayAction(myState.hand.cards().stream()
                    .filter(c -> c instanceof Land)
                    .findFirst()
                    .get());
            } catch (NoSuchElementException ignored) {
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
