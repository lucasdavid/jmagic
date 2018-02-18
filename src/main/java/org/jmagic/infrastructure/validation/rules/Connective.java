package org.jmagic.infrastructure.validation.rules;

import java.util.Arrays;
import java.util.List;

/**
 * Connective Base Class.
 *
 * Defines a composed validation rule.
 * Check its subclasses for examples.
 *
 * @author ldavid
 */
abstract class Connective extends ValidationRule {
    final List<ValidationRule> innerRules;

    Connective(ValidationRule... innerRules) {
        this.innerRules = Arrays.asList(innerRules);
    }

    @Override
    public boolean equals(Object o) {
        try {
            return this == o || this.innerRules.equals(((Connective) o).innerRules);
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%s%s", getClass().getSimpleName(), innerRules);
    }
}
