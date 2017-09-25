package org.jmagic.infrastructure.validation.rules;

import org.jmagic.infrastructure.exceptions.ValidationException;
import org.jmagic.core.states.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Validation Rule Base.
 * <p>
 * Note: look for its subclasses.
 *
 * @author ldavid
 */
public abstract class ValidationRule {

    protected final Collection<String> errors;
    private boolean validated;

    protected ValidationRule() {
        this.errors = new ArrayList<>();
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
        Optional<String> error = errors.stream().findFirst();
        if (error.isPresent()) {
            throw new ValidationException(error.get());
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
            throw new IllegalStateException("you must first validate a rule before asking whether it's valid or not");
        }
    }

    public Collection<String> errors() {
        return this.errors;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
