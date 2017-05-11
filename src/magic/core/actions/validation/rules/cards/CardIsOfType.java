package magic.core.actions.validation.rules.cards;

import magic.core.cards.ICard;
import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;

/**
 * @author ldavid
 */
public class CardIsOfType extends ValidationRule {
    private final ICard card;
    private final Class<? extends ICard> type;

    public CardIsOfType(ICard card, Class<? extends ICard> type) {
        this.card = card;
        this.type = type;
    }

    @Override
    public void onValidate(State state) {
        if (!type.isInstance(card)) {
            errors.add(String.format("%s is not a %s", card, type));
        }
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", super.toString(), card, type.getSimpleName());
    }
}
