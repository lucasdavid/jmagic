package magic.infrastructure.validation.basic;

import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;

import java.util.Arrays;
import java.util.List;

/**
 * And Validation Rule Base.
 * <p>
 *
 * @author ldavid
 */
public class And extends ValidationRule {

    private final List<ValidationRule> innerRules;

    public And(ValidationRule... innerRules) {
        this.innerRules = Arrays.asList(innerRules);
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

    @Override
    public String toString() {
        return String.format("And(%s)", innerRules);
    }
}
