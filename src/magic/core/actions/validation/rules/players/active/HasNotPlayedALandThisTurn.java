package magic.core.actions.validation.rules.players.active;

import magic.core.actions.Action;
import magic.core.actions.PlayAction;
import magic.core.actions.validation.ValidationRule;
import magic.core.cards.lands.Land;
import magic.core.states.State;

public class HasNotPlayedALandThisTurn extends ValidationRule {

    @Override
    public void onValidate(State state) {
        // Validate that Player isFrom drawing only once in a turn!
        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;
        State.PlayerState p = state.activePlayerState();

        while (previous != null && previous.turn == state.turn) {
            if (p.player.equals(previous.turnsPlayerState().player)
                && actionExecutedInPrevious instanceof PlayAction
                && ((PlayAction) actionExecutedInPrevious).card() instanceof Land) {
                errors.add(String.format("%s already played a land in this turn", p.player));
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }
    }
}
