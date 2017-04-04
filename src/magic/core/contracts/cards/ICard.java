package magic.core.contracts.cards;


import magic.core.State;
import magic.core.cards.lands.BasicLands;
import magic.core.contracts.IIdentifiable;
import magic.core.contracts.ITargetable;
import magic.core.exceptions.JMagicException;

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
     */
    ICard duplicate();

    Collection<BasicLands> cost();
}
