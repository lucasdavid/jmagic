package magic.players;

import magic.core.actions.Action;
import magic.core.actions.PlayAction;
import magic.core.actions.validation.rules.players.active.HasLandsToPlayIt;
import magic.core.cards.lands.Land;
import magic.core.experts.IExpert;
import magic.core.states.State;
import magic.core.states.TurnStep;

import java.util.NoSuchElementException;

public class NaivePlayer extends BasicPlayer {

    public NaivePlayer(IExpert mulliganExpert) {
        super(mulliganExpert);
    }

    public NaivePlayer(String name, IExpert mulliganExpert) {
        super(name, mulliganExpert);
    }

    @Override
    public Action act(State state) {
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
