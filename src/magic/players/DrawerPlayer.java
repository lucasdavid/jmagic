package magic.players;

import magic.core.Player;
import magic.core.State;
import magic.core.actions.Action;
import magic.core.actions.DiscardAction;
import magic.core.actions.DrawAction;
import magic.core.actions.PassAction;
import magic.core.actions.validation.HasNotAlreadyDrawnInThisTurn;

public class DrawerPlayer extends Player {

    public DrawerPlayer(String name) {
        super(name);
    }

    @Override
    public Action act(State state) {
        if (state.currentPlayerState().player != this) {
            // Not my turn to play.
            return null;
        }

        State.PlayerState myState = state.playerState(this);

        if (myState.hand.size() > 7) {
            return new DiscardAction(myState.hand.cards().get(0));
        }

        return new HasNotAlreadyDrawnInThisTurn().validate(state, this).isValid()
                ? new DrawAction()
                : new PassAction();
    }
}
