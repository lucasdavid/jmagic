package hearthstone.core.contracts;

import hearthstone.core.cards.magics.Attachment;

/**
 * Buffable Interface.
 *
 * @author ldavid
 */
public interface IAttachable extends ITargetable {

    IAttachable attach(Attachment attachment);

    IAttachable detach(Attachment attachment);
}
