package magic.players;

import magic.core.Player;
import magic.core.State;
import magic.core.actions.Action;

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

    @Override
    public Action act(State s) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
