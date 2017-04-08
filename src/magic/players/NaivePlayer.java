package magic.players;

import magic.core.actions.Action;
import magic.core.states.State;
import magic.core.states.TurnStep;

public class NaivePlayer extends DrawerPlayer {

    public NaivePlayer() {
        super();
    }

    public NaivePlayer(String name) {
        super(name);
    }

    @Override
    public Action act(State state) {
        if (state.turnsPlayerState().player.equals(this)
            && state.step == TurnStep.DECLARE_BLOCKERS) {
            // Declare blockers...
        }

        // Don't know what to do. Ask for the superclass.
        return super.act(state);
    }
}
