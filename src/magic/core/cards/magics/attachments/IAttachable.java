package magic.core.cards.magics.attachments;

import magic.core.cards.ICard;

/**
 * Attachable Interface.
 *
 * @author ldavid
 */
public interface IAttachable extends ICard {

    IAttachable attach(Boost attachment);

    IAttachable detach(Boost attachment);
}
