package org.jmagic.actions.validation.rules.players;

import org.jmagic.players.Player;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;

import java.util.Collection;
import java.util.List;


public class PlayerIs extends ValidationRule {

    private final Collection<Player> expected;

    public PlayerIs(Player... expected) {
        this(List.of(expected));
    }

    public PlayerIs(Collection<Player> expected) {
        this.expected = expected;
    }

    @Override
    public void onValidate(State state) {
        Player actual = state.activePlayerState().player;

        for (Player _expected : expected) {
            if (!actual.equals(_expected)) {
                errors.add(String.format("Active expected {%s} and expected {%s} are not the same",
                    actual, _expected));
            }
        }
    }
}
