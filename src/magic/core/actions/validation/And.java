package magic.core.actions.validation;

import magic.core.states.State;

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
            if (!r.validate(state).isValid()) {
                errors.addAll(r.errors);
                break;
            }
        }
    }
}
