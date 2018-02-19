package org.jmagic.actions.rules.cards;

import org.jmagic.core.cards.ICard;
import org.jmagic.core.cards.Properties;
import org.jmagic.core.states.State;
import org.jmagic.experts.CardFinderExpert;
import org.jmagic.infrastructure.validation.rules.ValidationRule;

public class CardHasAbility extends ValidationRule {

    private final ICard card;
    private final Properties ability;
    private final CardFinderExpert expert;

    public CardHasAbility(ICard card, Properties ability) {
        this(card, ability, new CardFinderExpert(card));
    }

    public CardHasAbility(ICard card, Properties ability, CardFinderExpert expert) {
        this.card = card;
        this.ability = ability;
        this.expert = expert;
    }

    @Override
    public void onValidate(State state) {
        ICard card = expert.find(state);

        if (!card.properties().contains(ability)) {
            errors.add(String.format("%s doesn't have the ability {%s}",
                card, ability));
        }
    }

}
