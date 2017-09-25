package org.jmagic.infrastructure.validation.basic;

import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Or Validation Rule Base.
 * <p>
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
