package org.games.jmagic.infrastructure.validation.basic;

import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;

/**
 * Not Validation Rule Base.
 * <p>
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