package magic.core.contracts.cards.attachments;

import magic.core.cards.magics.attachments.DamageLifeBoost;
import magic.core.contracts.cards.ICard;

/**
 * Attachable Interface.
 *
 * @author ldavid
 */
public interface IAttachable extends ICard {

    IAttachable attach(DamageLifeBoost attachment);

    IAttachable detach(DamageLifeBoost attachment);
}
