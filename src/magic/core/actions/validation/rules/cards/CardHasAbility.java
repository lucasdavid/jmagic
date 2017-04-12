package magic.core.actions.validation.rules.cards;

import magic.core.actions.validation.ValidationRule;
import magic.core.cards.ICard;
import magic.core.cards.Properties;
import magic.core.cards.creatures.Creature;
import magic.core.states.State;

public class CardHasAbility extends ValidationRule {

    private final ICard card;
    private final Properties ability;

    public CardHasAbility(ICard card, Properties ability) {
        this.card = card;
        this.ability = ability;
    }

    @Override
    public void onValidate(State state) {
        try {
            if (((Creature) card).properties().contains(ability)) return;
        } catch (ClassCastException ignored) {
        }

        errors.add(String.format("%s doesn't have the ability {%s}", card, ability));
    }
}
