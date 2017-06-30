package org.games.jmagic.infrastructure.validation.basic;

import org.games.jmagic.infrastructure.validation.rules.ValidationRule;

import java.util.Arrays;
import java.util.List;

/**
 * Connective Base Class.
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
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), innerRules);
    }
}
