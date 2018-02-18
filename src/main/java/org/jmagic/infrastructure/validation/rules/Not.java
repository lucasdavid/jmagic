package org.jmagic.infrastructure.validation.rules;

import org.jmagic.core.states.State;

/**
 * Not Validation Rule.
 * <p>
 * Define the logical-not basic rule.
 *
 * Usage example:
 * <pre>
 * {@code
 * ValidationRule r = new Not(new HasNotPlayedALandThisTurn());
 * }
 * </pre>
 *
 * @author ldavid
 */
public class Not extends Connective {

    public Not(ValidationRule innerRule) {
        super(innerRule);
    }

    @Override
    public void onValidate(State state) {
        if (innerRules.get(0).isValid(state)) {
            errors.add(String.format("Not(%s) failed", innerRules.get(0)));
        }
    }

}
