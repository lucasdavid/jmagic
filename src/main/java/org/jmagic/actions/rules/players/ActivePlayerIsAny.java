package org.jmagic.actions.rules.players;

import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.Player;

import java.util.Collection;
import java.util.List;


public class ActivePlayerIsAny extends ValidationRule {

    private final Collection<Player> expected;

    public ActivePlayerIsAny(Player... expected) {
        this(List.of(expected));
    }

    public ActivePlayerIsAny(Collection<Player> expected) {
        this.expected = expected;

        if (this.expected.isEmpty()) {
            throw new IllegalArgumentException("at least one player should be passed to ActivePlayerIsAny rule");
        }
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
