package hearthstone.core.cards;

import hearthstone.core.ITargetable;
import hearthstone.core.State;
import hearthstone.core.exceptions.HearthStoneException;
import hearthstone.core.exceptions.IllegalCardUsageException;

import java.util.List;
import java.util.UUID;


public abstract class SingleHarmful extends Harmful {
    public SingleHarmful(String name, int damage, int manaCost) {
        super(name, damage, manaCost);
    }

    public SingleHarmful(UUID id, String name, int damage, int manaCost) {
        super(id, name, damage, manaCost);
    }

    @Override
    public void validUseOrRaisesException(State state, List<ITargetable> targets)
            throws HearthStoneException {
        super.validUseOrRaisesException(state, targets);

        if (targets.size() != 1) {
            throw new IllegalCardUsageException(this
                    + " can burn exactly one target");
        }
    }
}
