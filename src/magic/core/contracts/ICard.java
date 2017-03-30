package magic.core.contracts;


import magic.core.State;
import magic.core.exceptions.MagicException;

import java.util.List;
import java.util.UUID;

/**
 * Card Interface.
 *
 * @author ldavid
 */
public interface ICard extends ITargetable {

    State use(State state, List<ITargetable> targets);

    void validUseOrRaisesException(State state, List<ITargetable> targets) throws MagicException;

    ICard duplicate();

    UUID id();

    String name();

    int cost();
}
