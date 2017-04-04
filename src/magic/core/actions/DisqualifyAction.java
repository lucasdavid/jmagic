package magic.core.actions;

import magic.core.Player;
import magic.core.State;
import magic.core.actions.validation.IsPlaying;
import magic.core.actions.validation.ValidationRule;

import java.util.Collection;
import java.util.List;

/**
 * Disqualify a player from the game.
 *
 * @author ldavid
 */
public class DisqualifyAction extends Action {

    @Override
    public State update(State state, Player actor) {
        List<State.PlayerState> players = state.playerStates();
        State.PlayerState p = state.playerState(actor);

        p = new State.PlayerState(p.player, p.life(), p.maxLife(),
                p.deck, p.hand, p.field, p.graveyard, false);
        players.set(players.indexOf(p), p);

        return new State(players, state.turn, state.done,
                state.turnsCurrentPlayerIndex, this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return List.of(
                new IsPlaying());
    }
}
