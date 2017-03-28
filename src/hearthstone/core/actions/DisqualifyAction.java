package hearthstone.core.actions;

import hearthstone.core.State;
import hearthstone.core.exceptions.HearthStoneException;

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
        State.PlayerInfo info = players.remove(playerId);
        info = new State.PlayerInfo(info.player, info.life,
                info.deck, info.hand, info.field, info.graveyard, false);
        players.add(playerId, info);

        return new State(players, state.turn, state.done,
                state.turnsCurrentPlayerId, this, state);
    }

    @Override
    public void validActionOrRaisesException(State state) throws HearthStoneException {
        if (!state.playerInfo(playerId).playing) {
            throw new HearthStoneException(String.format(
                    "cannot disqualify player %d, because they aren't playing",
                    playerId));
        }
    }
}
