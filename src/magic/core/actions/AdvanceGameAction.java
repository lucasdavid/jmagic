package magic.core.actions;

import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;
import magic.core.states.TurnStep;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Advance Turn Action.
 * <p>
 * Action used by a player to advance turn.
 *
 * @author ldavid
 */
public class AdvanceGameAction extends Action {

    @Override
    public State update(State state) {
        List<State.PlayerState> players = state.playerStates();

        int lastPlayerIndex = state.turnsPlayerIndex == 0
            ? state.playerStates().size() - 1
            : state.turnsPlayerIndex - 1;

        if (state.activePlayerIndex == lastPlayerIndex) {
            // Last player has requested turn advancement.
            if (state.step == TurnStep.CLEANUP) {
                // That was the last step of the turn's current player.
                // Finish and move onto the next player.
                int turnsPlayer = (state.turnsPlayerIndex + 1) % players.size();

                return new State(players, state.turn + 1, TurnStep.values()[0], state.done,
                    turnsPlayer, turnsPlayer);
            }

            // Go to next step of this turn.
            return new State(players, state.turn, state.step.next(), state.done,
                state.turnsPlayerIndex, state.turnsPlayerIndex, this, state);
        }

        // Change active player to the next alive.
        int activePlayerIndex = state.activePlayerIndex;
        do {
            activePlayerIndex = (activePlayerIndex + 1) % players.size();
        } while (!state.playerState(activePlayerIndex).isAlive());

        return new State(players, state.turn, state.step, state.done,
            state.turnsPlayerIndex, activePlayerIndex, this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return Collections.emptyList();
//        return List.of(
//            new Or(
//                new And(),
//                new And()
//            ));
//        new CardsCountInTurnsPlayersHandIsLessThan(7),
//            new OtherPlayersAreAlive());
    }
}
