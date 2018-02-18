package org.jmagic.infrastructure.validation.rules;

import org.jmagic.core.states.State;

import java.util.stream.Collectors;

/**
 * Or Validation Rule.
 * <p>
 * Define the logical-or connective.
 *
 * Usage example:
 * <pre>
 * {@code
 * ValidationRule r = new Or(new HasCardInHand(card),
 *                           new HasLandsToPlayIt(card));
 * }
 * </pre>
 *
 * @author ldavid
 */
public class Or extends Connective {

    public Or(ValidationRule... innerRules) {
        super(innerRules);
    }

    @Override
    public void onValidate(State state) {
        for (ValidationRule r : innerRules) {
            if (r.isValid(state)) return;
        }

        errors.add(String.format("Or(%s) failed: (%s)",
            innerRules, innerRules.stream()
                .flatMap(r -> r.errors().stream())
                .collect(Collectors.toList())));
    }

}
