package magic.core.actions.validation.rules.players;

import magic.core.Player;
import magic.core.actions.Action;
import magic.core.actions.InitialDrawAction;
import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

public class HasInitiallyDrawn extends ValidationRule {

    private final Player player;

    public HasInitiallyDrawn() {
        this(null);
    }

    public HasInitiallyDrawn(Player player) {
        this.player = player;
    }

    @Override
    public void onValidate(State state) {
        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;
        State.PlayerState p = player == null
            ? state.activePlayerState()
            : state.playerState(player);

        while (previous != null && previous.turn == state.turn) {
            if (p.player.equals(previous.activePlayerState().player)
                && actionExecutedInPrevious instanceof InitialDrawAction) {
                return;
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }

        errors.add(String.format("%s has not initially drawn", p.player));
    }
}
