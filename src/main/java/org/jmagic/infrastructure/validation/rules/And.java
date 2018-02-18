package org.jmagic.infrastructure.validation.rules;

import org.jmagic.core.states.State;

/**
 * And Validation Rule.
 * <p>
 * Define the logical-and connective.
 *
 * Usage example:
 * <pre>
 * {@code
 * ValidationRule r = new And(new HasCardInHand(card),
 *                            new HasLandsToPlayIt(card));
 * }
 * </pre>
 *
 * @author ldavid
 */
public class And extends Connective {

    public And(ValidationRule... innerRules) {
        super(innerRules);
    }

    @Override
    public void onValidate(State state) {
        for (ValidationRule r : innerRules) {
            if (!r.isValid(state)) {
                errors.addAll(r.errors());
                break;
            }
        }
    }
}
