package magic.core.actions.validation;

import magic.core.State;
import magic.core.cards.lands.BasicLands;
import magic.core.cards.lands.Land;
import magic.core.contracts.ICard;

import java.util.Collection;

public class PlayerHasLandsToPlay extends ValidationRule {

    private final ICard card;

    public PlayerHasLandsToPlay(ICard card) {
        this.card = card;
    }

    @Override
    public void onValidate(State state) {
        Collection<BasicLands> cost = card.cost();

        state.currentPlayerState().field.cards().stream()
                .filter(c -> c instanceof Land && !((Land) c).used())
                .map(c -> ((Land) c).kind())
                .forEach(cost::remove);

        if (!cost.isEmpty()) {
            errors.add(String.format("{%s} doesn't have the correct combination of lands to play {%s}",
                    state.currentPlayerState().player, card));
        }
    }
}
