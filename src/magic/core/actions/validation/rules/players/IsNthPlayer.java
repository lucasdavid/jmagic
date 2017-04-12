package magic.core.actions.validation.rules.players;

import magic.core.Player;
import magic.core.actions.validation.ValidationRule;
import magic.core.states.State;

import java.util.List;

public class IsNthPlayer extends ValidationRule {

    private final Player player;
    private final int n;

    public IsNthPlayer(int n) {
        this(null, n);
    }

    public IsNthPlayer(Player player, int n) {
        this.player = player;
        this.n = n;
    }

    @Override
    public void onValidate(State state) {
        List<State.PlayerState> ps = state.playerStates();
        State.PlayerState p = player != null ? state.playerState(player) : state.activePlayerState();
        int actualN = n >= 0 ? n : ps.size() - n;

        if (ps.indexOf(p) != actualN) {
            errors.add(String.format("%s is not the %d-th player",
                p.player, n));
        }
    }
}
