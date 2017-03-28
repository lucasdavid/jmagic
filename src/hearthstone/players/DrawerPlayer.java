package hearthstone.players;

import hearthstone.core.Player;
import hearthstone.core.State;
import hearthstone.core.actions.Action;
import hearthstone.core.actions.DrawAction;
import hearthstone.core.actions.PassAction;
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
                && s.parent.turnsCurrentPlayerId == s.turnsCurrentPlayerId) {
            return new PassAction();
        }

        return new DrawAction();
    }
}
