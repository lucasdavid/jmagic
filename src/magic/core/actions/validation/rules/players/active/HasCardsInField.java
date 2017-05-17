package magic.core.actions.validation.rules.players.active;

import magic.core.cards.ICard;
import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;

import java.util.Collection;
import java.util.List;


public class HasCardsInField extends ValidationRule {

    private final Collection<ICard> cards;

    public HasCardsInField(ICard cards) {
        this(List.of(cards));
    }

    public HasCardsInField(Collection<ICard> cards) {
        this.cards = cards;
    }

    @Override
    public void onValidate(State state) {
        State.PlayerState p = state.activePlayerState();

        if (cards.stream().anyMatch(c -> !p.field.contains(c))) {
            errors.add(String.format("{%s} doesn't have {%s} in their field",
                p.player, cards));
        }
    }
}
