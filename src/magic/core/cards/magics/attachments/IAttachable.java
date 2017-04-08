package magic.core.cards.magics.attachments;

import magic.core.cards.ICard;

/**
 * Attachable Interface.
 *
 * @author ldavid
 */
public interface IAttachable extends ICard {

    IAttachable attach(DamageLifeBoost attachment);

    IAttachable detach(DamageLifeBoost attachment);
}
