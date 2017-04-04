package magic.core.actions.validation;

import magic.core.Player;
import magic.core.State;

public class HasAtLeastNPeopleAlive extends ValidationRule {

    private final int n;

    public HasAtLeastNPeopleAlive(int n) {
        this.n = n;
    }

    @Override
    public void onValidate(State state, Player actor) {
        if (state.playerStates().stream().filter(State.PlayerState::isAlive).count() < n) {
            errors.add(String.format("game doesn't have at least %d people alive", n));
        }
    }
}
