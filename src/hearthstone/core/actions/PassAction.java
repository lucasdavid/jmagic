package hearthstone.core.actions;

import hearthstone.core.State;
import hearthstone.core.exceptions.HearthStoneException;
import hearthstone.core.exceptions.InvalidActionException;

/**
 *
 * @author ldavid
 */
public final class PassAction extends Action {

    @Override
    public State update(State state) {
        int id = state.turnsCurrentPlayerId + 1,
                turn = state.turn;

        if (id == state.getPlayerInfos().size()) {
            id = 0;
            turn += 1;
        }

        return new State(state.getPlayerInfos(), turn, state.done, id, this, state);
    }

    @Override
    public void validActionOrRaisesException(State state) throws HearthStoneException {
        // Make sure user didn't pass without drawing first.
        State previous = state.parent;
        Action actionInPrevious = state.actionThatLedToThisState;

        while (previous.turnsCurrentPlayerId == state.turnsCurrentPlayerId) {
            if (actionInPrevious instanceof DrawAction) {
                return;
            }

            actionInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }

        throw new InvalidActionException("cannot pass without drawing first!");
    }

}
