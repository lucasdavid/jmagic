package magic.core.actions.validation;

import magic.core.State;

import java.util.stream.IntStream;

public class AnotherPersonIsAlive extends ValidationRule {

    @Override
    public void onValidate(State state) {
        if (IntStream.range(0, state.playerStates().size())
                .noneMatch(i -> i != state.turnsCurrentPlayerIndex && state.playerState(i).isAlive())) {
            errors.add("passing requires that a person different than the current one to be playing");
        }
    }
}
