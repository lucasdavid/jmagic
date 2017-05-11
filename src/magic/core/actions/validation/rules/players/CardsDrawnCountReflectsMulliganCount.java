package magic.core.actions.validation.rules.players;

import magic.core.Player;
import magic.core.experts.MulliganExpert;
import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;

public class CardsDrawnCountReflectsMulliganCount extends ValidationRule {

    private final Player player;
    private final int cardsDrawnCount;

    public CardsDrawnCountReflectsMulliganCount(int cardsDrawnCount) {
        this(null, cardsDrawnCount);
    }

    public CardsDrawnCountReflectsMulliganCount(Player player, int cardsDrawnCount) {
        this.player = player;
        this.cardsDrawnCount = cardsDrawnCount;
    }

    @Override
    public void onValidate(State state) {
        Player player = this.player == null
            ? state.activePlayerState().player
            : this.player;

        int initialDrawsCount = new MulliganExpert().count(state, player);

        if (7 - initialDrawsCount != cardsDrawnCount) {
            errors.add(String.format("As this is %s's %dth mulligan, they"
                    + " should have drawn %d cards (actual: %d)",
                player, initialDrawsCount, 7 - initialDrawsCount, cardsDrawnCount));
        }
    }
}
