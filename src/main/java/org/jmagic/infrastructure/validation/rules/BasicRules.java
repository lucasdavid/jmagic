package org.jmagic.infrastructure.validation.rules;

import org.jmagic.core.states.State;

import java.util.function.Predicate;

/**
 * Validation Rules.
 *
 * @author ldavid
 */
public abstract class BasicRules {

    public static ValidationRule Or(ValidationRule... innerRules) {
        return new Or(innerRules);
    }

    public static ValidationRule And(ValidationRule... innerRules) {
        return new And(innerRules);
    }

    public static ValidationRule Not(ValidationRule innerRule) {
        return new Not(innerRule);
    }

    public static ValidationRule IsTrue(Predicate<State> predicate) {
        return new IsTrue(predicate);
    }
}
