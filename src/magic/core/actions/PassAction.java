package magic.core.actions;

import magic.core.State;
import magic.core.exceptions.MagicException;
import magic.core.exceptions.InvalidActionException;

/**
 * Pass Action.
 * <p>
 * Action used by a player to pass their turn.
 *
 * @author ldavid
 */
public class PassAction extends Action {

    @Override
    public State update(State state) {
        int id = state.turnsCurrentPlayerId;
        int turn = state.turn;

        do {
            // set the turn's current player to the next player who's current playing.
            if (++id == state.getPlayersInfo().size()) {
                // All players were analyzed. Turn it over.
                id = 0;
                turn += 1;
            }
        } while (!state.playerInfo(id).playing);

        return new State(state.getPlayersInfo(), turn, state.done, id, this, state);
    }

    @Override
    public void raiseForErrors(State state) throws MagicException {
        if (state.getPlayersInfo().size() < 2) {
            throw new InvalidActionException("passing requires at least two people");
        }
        if (state.getPlayersInfo().stream().noneMatch(i -> i.playing)) {
            throw new InvalidActionException("at least one player must be standing in order to pass");
        }
    }
}
