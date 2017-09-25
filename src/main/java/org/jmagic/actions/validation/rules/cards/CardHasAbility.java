package org.jmagic.actions.validation.rules.cards;

import org.jmagic.core.cards.ICard;
import org.jmagic.core.cards.Properties;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;

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
