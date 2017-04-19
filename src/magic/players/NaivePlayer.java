package magic.players;

import magic.core.actions.Action;
import magic.core.actions.AdvanceGameAction;
import magic.core.actions.PlayAction;
import magic.core.actions.validation.rules.players.active.HasLandsToPlayIt;
import magic.core.cards.ICard;
import magic.core.cards.lands.Land;
import magic.core.states.State;
import magic.core.states.TurnStep;

import java.util.NoSuchElementException;

public class NaivePlayer extends BasicPlayer {

    public NaivePlayer() {
        super();
    }

    public NaivePlayer(String name) {
        super(name);
    }

    @Override
    public Action act(State state) {
        if (!state.turnsPlayerState().player.equals(this)) {
            // Don't intercept other people's turns.
            return new AdvanceGameAction();
        }

        State.PlayerState myState = state.playerState(this);

        if (state.step == TurnStep.MAIN_2) {
            try {
                return new PlayAction(myState.hand.cards().stream()
                    .filter(c -> !(c instanceof Land) && new HasLandsToPlayIt(this, c).isValid(state))
                    .findAny()
                    .get());
            } catch (NoSuchElementException ignored) {
            }
        }

        if (state.step == TurnStep.DECLARE_BLOCKERS) {
            // Declare blockers...
        }

        // Don't know what to do. Ask for the superclass.
        return super.act(state);
    }
}
