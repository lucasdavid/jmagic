package magic.infrastructure.validation.basic;

import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;

/**
 * Not Validation Rule Base.
 * <p>
 *
 * @author ldavid
 */
public class Not extends ValidationRule {

    private final ValidationRule innerRule;

    public Not(ValidationRule innerRule) {
        this.innerRule = innerRule;
    }

    @Override
    public void onValidate(State state) {
        if (innerRule.isValid(state)) {
            errors.add(String.format("Not(%s) is invalid", innerRule));
        }
    }

    @Override
    public String toString() {
        return String.format("Not(%s)", innerRule);
    }
}
