package magic.core.cards;


import magic.core.IIdentifiable;
import magic.core.ITargetable;
import magic.core.cards.lands.BasicLands;
import magic.core.exceptions.JMagicException;
import magic.core.states.State;

import java.util.Collection;
import java.util.List;

/**
 * Card Interface.
 *
 * @author ldavid
 */
public interface ICard extends ITargetable, IIdentifiable {

    State use(State state, List<ITargetable> targets);

    void raiseForErrors(State state, List<ITargetable> targets) throws JMagicException;

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
