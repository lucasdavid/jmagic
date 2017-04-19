package magic.core.actions.validation;

import magic.core.states.State;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        errors.addAll(innerRules.stream()
                .flatMap(r -> r.validate(state).errors().stream())
                .collect(Collectors.toList()));
    }
}
