package magic.core.actions;

import magic.core.actions.validation.rules.game.TurnIs;
import magic.core.actions.validation.rules.game.TurnsStepIs;
import magic.core.actions.validation.rules.players.HasPerformedThisTurn;
import magic.core.actions.validation.rules.players.PlayersOtherThanActiveAreAlive;
import magic.core.states.State;
import magic.core.states.TurnSteps;
import magic.infrastructure.validation.rules.ValidationRule;

import java.util.List;

import static magic.infrastructure.validation.connectives.Connectives.And;
import static magic.infrastructure.validation.connectives.Connectives.Not;
import static magic.infrastructure.validation.connectives.Connectives.Or;

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

            if (state.step == TurnSteps.CLEANUP || !state.turnsPlayerState().isAlive()) {
                // That was the last step of the turn's current player or they died.
                // Change turn's player to the next alive.
                int turnsPlayer = state.turnsPlayerIndex;
                do {
                    turnsPlayer = (turnsPlayer + 1) % players.size();
                } while (!state.playerState(turnsPlayer).isAlive());

                return new State(players, state.turn + 1, TurnSteps.values()[0], state.done,
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
    public ValidationRule validationRules() {
        return And(
            new PlayersOtherThanActiveAreAlive(),
            // Prevents players from advancing without initially drawing 7 cards.
            Or(
                Not(And(
                    new TurnIs(0),
                    new TurnsStepIs(TurnSteps.DRAW))),
                new HasPerformedThisTurn(InitialDrawAction.class)));
    }
}
