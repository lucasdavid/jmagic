package magic.core.actions.validation.rules.cards;

import magic.core.cards.ICard;
import magic.core.cards.Properties;
import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;

public class CardHasAbility extends ValidationRule {

    private final ICard card;
    private final Properties ability;

    public CardHasAbility(ICard card, Properties ability) {
        this.card = card;
        this.ability = ability;
    }

    @Override
    public void onValidate(State state) {
        if (!card.properties().contains(ability)) {
            errors.add(String.format("%s doesn't have the ability {%s}", card, ability));
        }
    }
}
