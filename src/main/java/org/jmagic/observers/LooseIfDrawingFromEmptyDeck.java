package org.jmagic.observers;

import org.jmagic.actions.Action;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;

/**
 * LooseIfDrawingFromEmptyDeck Observer.
 * <p>
 * Adding this to a game's observer pool will result in a player loosing when
 * attempting to draw from an empty deck.
 *
 * @author ldavid
 */
public class LooseIfDrawingFromEmptyDeck extends Observer {
    @Override
    public State afterPlayerAct(State state, Action action, long actStartedAt, long actEndedAt) {
        State.PlayerState p = state.activePlayerState();

        if (state.step == TurnSteps.DRAW && p.deck.isEmpty()
                && state.turnsPlayerIndex == state.activePlayerIndex) {

            LOG.warning(String.format(
                    "%s lost because `LooseIfDrawingFromEmptyDeck` rule is active",
                    p.player));

            return disqualify(state, p.player);
        }

        return state;
    }
}
