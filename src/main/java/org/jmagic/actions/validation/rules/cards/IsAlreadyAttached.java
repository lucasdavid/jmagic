package org.jmagic.actions.validation.rules.cards;

import org.jmagic.core.cards.attachments.IAttachable;
import org.jmagic.core.cards.attachments.IAttachment;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;

/**
 * Has Valid Target Validation Rule.
 *
 * @author ldavid
 */
public class IsAlreadyAttached extends ValidationRule {

    private final IAttachment attachment;

    public IsAlreadyAttached(IAttachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public void onValidate(State state) {
        state.playerStates().stream()
            .flatMap(p -> p.field.cards().stream())
            .filter(c -> c instanceof IAttachable)
            .map(c -> (IAttachable) c)
            .filter(a -> a.attached(attachment))
            .findAny()
            .ifPresent(a ->
                errors.add(String.format(
                    "%s is already attached to a %s", attachment, a)));
    }
}
