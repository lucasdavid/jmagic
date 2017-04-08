package magic.core.actions.validation.rules;

import magic.core.actions.validation.ValidationRule;
import magic.core.cards.ICard;
import magic.core.cards.lands.BasicLands;
import magic.core.cards.lands.Land;
import magic.core.states.State;

import java.util.Collection;

public class HasLandsToPlayIt extends ValidationRule {

    private final ICard card;

    public HasLandsToPlayIt(ICard card) {
        this.card = card;
    }

    @Override
    public void onValidate(State state) {
        Collection<BasicLands> cost = card.cost();
        State.PlayerState activeState = state.activePlayerState();

        activeState.field.cards().stream()
                .filter(c -> c instanceof Land && !((Land) c).used())
                .map(c -> ((Land) c).kind())
                .forEach(cost::remove);

        if (!cost.isEmpty()) {
            errors.add(String.format("{%s} doesn't have the correct combination of lands to play {%s}",
                activeState.player, card));
        }
    }
}
