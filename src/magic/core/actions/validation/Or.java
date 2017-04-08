package magic.core.actions.validation;

import magic.core.states.State;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Or Validation Rule Base.
 * <p>
 *
 * @author ldavid
 */
public class Or extends ValidationRule {

    private final List<ValidationRule> innerRules;

    public Or(ValidationRule... innerRules) {
        this.innerRules = Arrays.asList(innerRules);
    }

    @Override
    public void onValidate(State state) {
        errors.addAll(this.innerRules.stream()
                .map(r -> r.validate(state).errors())
                .filter(Collection::isEmpty)
                .findFirst()
                .orElseGet(() -> this.innerRules.get(0).errors()));
    }
}
