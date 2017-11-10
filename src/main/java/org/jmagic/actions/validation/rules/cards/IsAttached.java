package org.jmagic.actions.validation.rules.cards;

import org.jmagic.core.cards.attachments.IAttachable;
import org.jmagic.core.cards.attachments.IAttachment;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;

/**
 * Is Attached Validation Rule.
 *
 * @author ldavid
 */
public class IsAttached extends ValidationRule {

    private final IAttachment attachment;

    public IsAttached(IAttachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public void onValidate(State state) {
        boolean isNotAttached = state.playerStates().stream()
            .flatMap(p -> p.field.cards().stream())
            .filter(c -> c instanceof IAttachable)
            .map(c -> (IAttachable) c)
            .noneMatch(a -> a.attached(attachment));

        if (isNotAttached) {
            errors.add(String.format("%s is not attached to any card", attachment));
        }
    }
}
