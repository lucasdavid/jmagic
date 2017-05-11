package magic.core.cards.validation.rules;

import magic.core.ITargetable;
import magic.core.cards.magics.attachments.IAttachment;
import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;

import java.util.List;

/**
 * Has Valid Target Validation Rule.
 *
 * @author ldavid
 */
public class TargetsAreAttachable extends ValidationRule {

    private final IAttachment attachment;

    public TargetsAreAttachable(IAttachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public void onValidate(State state) {

    }
}
