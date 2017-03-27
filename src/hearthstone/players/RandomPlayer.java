package hearthstone.players;

import hearthstone.core.Player;
import hearthstone.core.State;
import hearthstone.core.actions.Action;
import hearthstone.core.actions.DrawAction;
import hearthstone.core.actions.PassAction;
import java.util.UUID;

/**
 * RandomPlayer.
 * Executes random actions independent from the current state of the game.
 *
 * @author ldavid
 */
public class RandomPlayer extends Player {

    public RandomPlayer(String name) {
        super(name);
    }

    public RandomPlayer(UUID id, String name) {
        super(id, name);
    }

    @Override
    public Action act(State s) {
        if (s.parent == null || s.parent.turnsCurrentPlayerId != s.turnsCurrentPlayerId) {
            return new DrawAction();
        }

        return new PassAction();
    }
}
