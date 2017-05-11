package magic.infrastructure.validation.connectives;

import magic.infrastructure.validation.rules.ValidationRule;

/**
 * Validation Rules.
 *
 * @author ldavid
 */
public abstract class Connectives {

    public static ValidationRule Or(ValidationRule... innerRules) {
        return new Or(innerRules);
    }

    public static ValidationRule And(ValidationRule... innerRules) {
        return new And(innerRules);
    }

    public static ValidationRule Not(ValidationRule innerRule) {
        return new Not(innerRule);
    }
}
