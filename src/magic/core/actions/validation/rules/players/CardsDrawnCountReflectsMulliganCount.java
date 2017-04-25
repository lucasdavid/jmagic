package magic.core.actions.validation.rules.players;

import magic.core.Player;
import magic.core.actions.Action;
import magic.core.actions.InitialDrawAction;
import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;
import magic.core.states.TurnStep;

public class CardsDrawnCountReflectsMulliganCount extends ValidationRule {

    private final Player player;
    private final int cardsDrawnCount;

    public CardsDrawnCountReflectsMulliganCount(int cardsDrawnCount) {
        this(null, cardsDrawnCount);
    }

    public CardsDrawnCountReflectsMulliganCount(Player player, int cardsDrawnCount) {
        this.player = player;
        this.cardsDrawnCount = cardsDrawnCount;
    }

    @Override
    public void onValidate(State state) {
        Action actionExecutedInPrevious = state.actionThatLedToThisState;
        State previous = state.parent;
        State.PlayerState p = player == null
            ? state.activePlayerState()
            : state.playerState(player);

        int initialDrawsCount = 0;

        assert state.step == TurnStep.DRAW;

        while (previous != null && previous.step == state.step) {
            if (p.player.equals(previous.activePlayerState().player)
                && actionExecutedInPrevious instanceof InitialDrawAction) {
                initialDrawsCount++;
            }

            actionExecutedInPrevious = previous.actionThatLedToThisState;
            previous = previous.parent;
        }

        if (7 - initialDrawsCount != cardsDrawnCount) {
            errors.add(String.format("As this is %s's %dth mulligan, they"
                    + " should have drawn %d cards (actual: %d)",
                p.player, initialDrawsCount, 7 - initialDrawsCount, cardsDrawnCount));
        }
    }
}
