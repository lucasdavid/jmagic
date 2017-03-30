package magic.core.actions;

import magic.core.State;
import magic.core.exceptions.MagicException;

/**
 * Finish a game.
 *
 * @author ldavid
 */
public class FinishGameAction extends Action {

    @Override
    public State update(State state) {
        return new State(state.getPlayersInfo(), state.turn, true,
                state.turnsCurrentPlayerId, this, state);
    }

    @Override
    public void raiseForErrors(State state) throws MagicException {
        if (state.done) {
            throw new MagicException("cannot finish a game that's already done");
        }
    }
}
