package magic.core.actions;

import magic.core.State;
import magic.core.actions.validation.ValidationRule;
import magic.core.actions.validation.IsPlaying;

import java.util.Collection;
import java.util.List;

/**
 * Disqualify a player from the game.
 *
 * @author ldavid
 */
public class DisqualifyAction extends Action {

    private final int playerId;

    public DisqualifyAction(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public State update(State state) {
        List<State.PlayerInfo> players = state.getPlayersInfo();
        State.PlayerInfo p = players.remove(playerId);
        p = new State.PlayerInfo(p.player, p.life, p.maxLife,
                p.deck, p.hand, p.field, p.graveyard, false);
        players.add(playerId, p);

        return new State(players, state.turn, state.done,
                state.turnsCurrentPlayerId, this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return List.of(
                new IsPlaying(playerId)
        );
    }
}
