package org.jmagic.actions.rules.players;

import org.jmagic.players.Player;
import org.jmagic.actions.Action;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;

public class HasPerformedThisTurn extends ValidationRule {

    private final Player player;
    private final Class<? extends Action> actionClass;

    public HasPerformedThisTurn(Class<? extends Action> actionClass) {
        this(actionClass, null);
    }

    public HasPerformedThisTurn(Class<? extends Action> actionClass, Player player) {
        this.player = player;
        this.actionClass = actionClass;
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
                && actionClass.isInstance(actionExecutedInPrevious)) {
                return;
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }

        errors.add(String.format("%s has not performed %s", p.player, actionClass));
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", super.toString(), actionClass.getSimpleName(),
            player == null ? "active" : player);
    }
}
