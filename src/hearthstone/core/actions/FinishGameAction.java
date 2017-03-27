package hearthstone.core.actions;

import hearthstone.core.State;
import hearthstone.core.exceptions.HearthStoneException;

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
    public void validActionOrRaisesException(State state) throws HearthStoneException {
        if (state.done) {
            throw new HearthStoneException("cannot finish a game that's already done");
        }
    }
}
