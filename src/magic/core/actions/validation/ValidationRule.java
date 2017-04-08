package magic.core.actions.validation;

import magic.core.exceptions.ValidationException;
import magic.core.states.State;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Validation Rule Base.
 * <p>
 * Note: look for its subclasses.
 *
 * @author ldavid
 */
public abstract class ValidationRule {

    private boolean validated;
    protected final Collection<String> errors;

    protected ValidationRule() {
        this.errors = new ArrayList<>();
        this.validated = false;
    }

    public abstract void onValidate(State state);

    public ValidationRule validate(State state) {
        this.validated = false;
        this.onValidate(state);
        this.validated = true;
        return this;
    }

    public void raiseForErrors() throws ValidationException {
        assertValidated();
        if (!errors.isEmpty()) {
            throw new ValidationException(errors.stream().findFirst().get());
        }
    }

    public void raiseForErrors(State state) throws ValidationException {
        validate(state).raiseForErrors();
    }

    public boolean isValid() {
        assertValidated();
        return this.errors.isEmpty();
    }

    public boolean isValid(State state) {
        return validate(state).isValid();
    }

    private void assertValidated() {
        if (!this.validated) {
            throw new IllegalStateException(
                    "you must first validate a rule before asking whether it's valid or not");
        }
    }

    public Collection<String> errors() {
        return new ArrayList<>(this.errors);
    }
}
