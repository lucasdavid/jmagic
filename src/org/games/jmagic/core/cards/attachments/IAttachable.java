package org.games.jmagic.core.cards.attachments;

import org.games.jmagic.core.cards.ICard;

/**
 * Attachable Interface.
 *
 * @author ldavid
 */
public interface IAttachable extends ICard {

    IAttachable attach(IAttachment attachment);

    IAttachable detach(IAttachment attachment);

    boolean attached(IAttachment attachment);
}
