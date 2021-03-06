package org.jmagic.core.cards;


import org.jmagic.infrastructure.IIdentifiable;
import org.jmagic.infrastructure.ITargetable;
import org.jmagic.core.cards.lands.BasicLands;

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
