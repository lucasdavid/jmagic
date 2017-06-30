package org.games.jmagic.infrastructure.validation.basic;

import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;

import java.util.Arrays;
import java.util.List;

/**
 * And Validation Rule Base.
 * <p>
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
