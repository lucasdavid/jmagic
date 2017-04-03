package magic.core.contracts;


import magic.core.State;
import magic.core.cards.lands.BasicLands;
import magic.core.exceptions.MagicException;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Card Interface.
 *
 * @author ldavid
 */
public interface ICard extends ITargetable, IIdentifiable {

    State use(State state, List<ITargetable> targets);

    void raiseForErrors(State state, List<ITargetable> targets) throws MagicException;

    ICard duplicate();

    Collection<BasicLands> cost();
}
