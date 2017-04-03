package magic.core.cards;

import magic.core.State;
import magic.core.cards.lands.BasicLands;
import magic.core.contracts.ITargetable;
import magic.core.exceptions.MagicException;
import magic.core.exceptions.IllegalCardUsageException;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


public abstract class SingleHarmful extends Harmful {

    public SingleHarmful(String name, int damage, Collection<BasicLands> cost) {
        super(name, damage, cost);
    }

    public SingleHarmful(UUID id, String name, int damage, Collection<BasicLands> cost) {
        super(id, name, damage, cost);
    }

    @Override
    public void validUseOrRaisesException(State state, List<ITargetable> targets)
            throws MagicException {
        super.validUseOrRaisesException(state, targets);

        if (targets.size() != 1) {
            throw new IllegalCardUsageException("can only burn exactly one target");
        }
    }
}
