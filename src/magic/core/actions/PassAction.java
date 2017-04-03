package magic.core.actions;

import magic.core.State;
import magic.core.actions.validation.AnotherPersonIsAlive;
import magic.core.actions.validation.ValidationRule;

import java.util.Collection;
import java.util.List;

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
        int id   = state.turnsCurrentPlayerIndex,
            turn = state.turn;

        do {
            // set the turn's current player to the next player who's current playing.
            if (++id == state.playerStates().size()) {
                // All players were analyzed. Turn it over.
                id = 0;
                turn += 1;
            }
        } while (!state.playerState(id).isAlive());

        return new State(state.playerStates(), turn, state.done, id, this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return List.of(
                new AnotherPersonIsAlive()
        );
    }
}
