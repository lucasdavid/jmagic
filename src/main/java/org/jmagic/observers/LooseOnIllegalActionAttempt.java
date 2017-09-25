package org.jmagic.observers;

import org.jmagic.actions.Action;
import org.jmagic.core.states.State;

import java.util.Collection;
import java.util.Set;

/**
 * LooseOnIllegalActionAttempt Observer.
 * <p>
 * Adding this to a game's observers pool will result in a player loosing when
 * they attempt an action that was not listed on the
 * {@link LooseOnIllegalActionAttempt#allowed} attribute here, set during this
 * class's construction.
 *
 * @author ldavid
 */
public class LooseOnIllegalActionAttempt extends Observer {

    private final Collection<Class<? extends Action>> allowed;

    public LooseOnIllegalActionAttempt(Class<? extends Action>... allowed) {
        this(Set.of(allowed));
    }

    public LooseOnIllegalActionAttempt(Collection<Class<? extends Action>> allowed) {
        this.allowed = allowed;
    }

    @Override
    public State afterPlayerAct(State state, Action action, long actStartedAt, long actEndedAt) {
        if (!allowed.contains(action.getClass())) {
            State.PlayerState p = state.activePlayerState();

            LOG.warning(String.format("%s lost because %s is not listed as a legal action",
                p.player, action));

            return _disqualify(state);
        }

        return state;
    }
}
