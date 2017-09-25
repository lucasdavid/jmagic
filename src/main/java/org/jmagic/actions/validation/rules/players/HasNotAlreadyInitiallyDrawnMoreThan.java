package org.jmagic.actions.validation.rules.players;

import org.jmagic.players.Player;
import org.jmagic.actions.Action;
import org.jmagic.actions.InitialDrawAction;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;


public class HasNotAlreadyInitiallyDrawnMoreThan extends ValidationRule {

    private final Player player;
    private final int count;

    public HasNotAlreadyInitiallyDrawnMoreThan(int count) {
        this(null, count);
    }

    public HasNotAlreadyInitiallyDrawnMoreThan(Player player, int count) {
        this.player = player;
        this.count = count;
    }

    @Override
    public void onValidate(State state) {
        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;
        State.PlayerState p = player == null
            ? state.activePlayerState()
            : state.playerState(player);

        int initialDrawsCount = 0;

        while (previous != null && previous.turn == state.turn) {
            if (p.player.equals(previous.activePlayerState().player)
                && actionExecutedInPrevious instanceof InitialDrawAction) {
                initialDrawsCount++;
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }

        if (initialDrawsCount > count) {
            errors.add(String.format("The maximum initial draw allowed is %d and %s drew %d times",
                count, p.player, initialDrawsCount));
        }
    }
}
