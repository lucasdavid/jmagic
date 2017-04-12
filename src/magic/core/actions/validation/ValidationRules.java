package magic.core.actions.validation;

/**
 * Validation Rules.
 *
 * @author ldavid
 */
public abstract class ValidationRules {

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
