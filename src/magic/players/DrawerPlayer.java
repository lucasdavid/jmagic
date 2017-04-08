package magic.players;

import magic.core.Player;
import magic.core.actions.Action;
import magic.core.actions.AdvanceGameAction;
import magic.core.actions.DiscardAction;
import magic.core.actions.DrawAction;
import magic.core.actions.validation.rules.ActivePlayerHasNotAlreadyDrawnInThisTurn;
import magic.core.states.State;
import magic.core.states.TurnStep;

public class DrawerPlayer extends Player {

    public DrawerPlayer() {
        super();
    }

    public DrawerPlayer(String name) {
        super(name);
    }

    @Override
    public Action act(State state) {
        if (!state.turnsPlayerState().player.equals(this)) {
            // Don't intercept other people's turns.
            return new AdvanceGameAction();
        }

        State.PlayerState myState = state.playerState(this);

        if (state.step == TurnStep.DRAW &&
            new ActivePlayerHasNotAlreadyDrawnInThisTurn().isValid(state)) {
            return new DrawAction();
        }

        if (state.step == TurnStep.CLEANUP &&
            myState.hand.size() > 7) {
            return new DiscardAction(myState.hand.cards().get(0));
        }

        // Just keep going.
        return new AdvanceGameAction();
    }
}
