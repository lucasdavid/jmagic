package magic.players;

import magic.core.Player;
import magic.core.State;
import magic.core.actions.Action;
import magic.core.actions.DrawAction;
import magic.core.actions.PassAction;
import java.util.UUID;

public class DrawerPlayer extends Player {

    public DrawerPlayer(String name) {
        super(name);
    }

    public DrawerPlayer(UUID id, String name) {
        super(id, name);
    }

    @Override
    public Action act(State s) {
        if (s.parent != null
                && s.parent.actionThatLedToThisState instanceof DrawAction
                && s.parent.turnsCurrentPlayerIndex == s.turnsCurrentPlayerIndex) {
            return new PassAction();
        }

        return new DrawAction();
    }
}
