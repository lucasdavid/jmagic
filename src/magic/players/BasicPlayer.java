package magic.players;

import magic.core.Player;
import magic.core.actions.Action;
import magic.core.actions.AdvanceGameAction;
import magic.core.actions.DiscardAction;
import magic.core.actions.DrawAction;
import magic.core.actions.PlayAction;
import magic.core.actions.UntapAction;
import magic.core.actions.validation.rules.players.active.HasNotAlreadyDrawnInThisTurn;
import magic.core.actions.validation.rules.players.active.HasNotAlreadyUntappedInThisTurn;
import magic.core.cards.lands.Land;
import magic.core.states.State;
import magic.core.states.TurnStep;

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

    public BasicPlayer() {
        super();
    }

    public BasicPlayer(String name) {
        super(name);
    }

    @Override
    public Action act(State state) {
        if (!state.turnsPlayerState().player.equals(this)) {
            // Don't intercept other people's turns.
            return new AdvanceGameAction();
        }

        State.PlayerState myState = state.playerState(this);

        if (state.step == TurnStep.DRAW && new HasNotAlreadyDrawnInThisTurn().isValid(state)) {
            return new DrawAction();
        }

        if (state.step == TurnStep.UNTAP && new HasNotAlreadyUntappedInThisTurn().isValid(state)) {
            return new UntapAction(this);
        }

        if (state.step == TurnStep.CLEANUP && myState.hand.size() > 7) {
            return new DiscardAction(myState.hand.cards().get(0));
        }

        if (state.step == TurnStep.MAIN_1) {
            try {
                return new PlayAction(myState.hand.cards().stream()
                    .filter(c -> c instanceof Land)
                    .findFirst()
                    .get());
            } catch (NoSuchElementException ignored) {}
        }

        // Can't do anything else right now.
        return new AdvanceGameAction();
    }
}
