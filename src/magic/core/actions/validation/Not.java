package magic.core.actions.validation;

import magic.core.states.State;

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
        innerRule.validate(state);
        if (innerRule.isValid()) {
            errors.add(String.format("inner rule %s should not be valid, but it is", innerRule));
        }
    }
}
