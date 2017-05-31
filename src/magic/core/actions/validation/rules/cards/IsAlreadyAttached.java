package magic.core.actions.validation.rules.cards;

import magic.core.cards.magics.attachments.IAttachable;
import magic.core.cards.magics.attachments.IAttachment;
import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;

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
