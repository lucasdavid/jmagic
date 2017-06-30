package org.games.jmagic.observers;

import org.games.jmagic.actions.Action;
import org.games.jmagic.core.states.State;
import org.games.jmagic.players.Player;

/**
 * LooseOnActTimeout Observer.
 * <p>
 * Adding this to a game observer's pool will result in a player loosing if
 * they take longer than {@link LooseOnActTimeout#timeout} to return from a
 * {@link Player#act} call.
 *
 * @author ldavid
 */
public class LooseOnActTimeout extends Observer {

    private final long timeout;

    public LooseOnActTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public State afterPlayerAct(State state, Action action, long actStartedAt, long actEndedAt) {
        if (actEndedAt - actStartedAt >= this.timeout) {
            State.PlayerState p = state.activePlayerState();

            LOG.warning(String.format(
                "%s lost because they exceeded the allowed act time-frame of %d",
                p.player, this.timeout));

            return _disqualify(state);
        }

        return state;
    }
}
