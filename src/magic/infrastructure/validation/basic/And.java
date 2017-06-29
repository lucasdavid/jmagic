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
