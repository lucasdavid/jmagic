package magic.core.actions.validation.rules.players;

import magic.core.cards.ICard;
import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class CardsAreInAnyField extends ValidationRule {

    private final Collection<ICard> cards;

    public CardsAreInAnyField(ICard cards) {
        this(List.of(cards));
    }

    public CardsAreInAnyField(Collection<ICard> cards) {
        this.cards = cards;
    }

    @Override
    public void onValidate(State state) {
        Set<ICard> cardsInFields = state.playerStates().stream()
            .flatMap(playerState -> playerState.field.cards().stream())
            .collect(Collectors.toSet());

        State.PlayerState p = state.activePlayerState();

        if (cards.stream().anyMatch(c -> !cardsInFields.contains(c))) {
            errors.add(String.format("{%s} doesn't have {%s} in their field",
                p.player, cards));
        }
    }
}