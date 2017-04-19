package magic.core.observers;

import magic.core.actions.InitialDrawAction;
import magic.core.exceptions.InvalidActionException;
import magic.core.states.State;

/**
 * @author ldavid
 */
public class StartGameWithHandSize extends Observer {

    private final int count;
    private boolean ran;

    public StartGameWithHandSize() {
        this(7);
    }

    public StartGameWithHandSize(int count) {
        this.count = count;
    }

    @Override
    public State beforePlayerAct(State state) {
        if (!ran) {
            LOG.info(String.format(
                "Each player is starting with %d cards because `StartGameWithHandSize` rule is active",
                count));

            try {
                state = new InitialDrawAction().raiseForErrors(state).update(state);
                ran = true;
            } catch (InvalidActionException e) {
            }
        }

        return state;
    }
}
