package magic.core.cards;


import magic.core.IIdentifiable;
import magic.core.ITargetable;
import magic.core.cards.lands.BasicLands;
import magic.infrastructure.validation.IGameModifier;

import java.util.Collection;

/**
 * Card Interface.
 *
 * @author ldavid
 */
public interface ICard extends ITargetable, IIdentifiable {

    /**
     * @return an exact duplicate of the card, except for the id.
     * This is useful for deck builders that want to copy a card.
     */
    ICard duplicate();

    Collection<Properties> properties();

    Collection<Properties> effectiveProperties();

    Collection<BasicLands> cost();

    String toString(boolean detailed);
}
