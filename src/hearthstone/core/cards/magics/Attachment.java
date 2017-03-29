package hearthstone.core.cards.magics;

import hearthstone.core.State;
import hearthstone.core.contracts.ITargetable;
import hearthstone.core.exceptions.HearthStoneException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class Attachment extends Effect {

    public static Collection<Attachment> DEFAULT_CARDS = Collections.unmodifiableCollection(Arrays.asList(
            new Attachment("Sword of Dawn", 2, 0, 1),
            new Attachment("Heavenly Blessing", 4, 2, 4)
    ));

    private final int damageIncrease;
    private final int lifeIncrease;

    public Attachment(String name, int damageIncrease, int lifeIncrease, int manaCost) {
        this(UUID.randomUUID(), name, damageIncrease, lifeIncrease, manaCost);
    }

    public Attachment(UUID id, String name, int damageIncrease, int lifeIncrease, int manaCost) {
        super(id, name, manaCost);

        this.damageIncrease = damageIncrease;
        this.lifeIncrease = lifeIncrease;
    }

    public int getDamageIncrease() {
        return damageIncrease;
    }

    public int getLifeIncrease() {
        return lifeIncrease;
    }

    @Override
    public State use(State state, List<ITargetable> targets) {
        throw new NotImplementedException();
    }

    @Override
    public void validUseOrRaisesException(State state, List<ITargetable> targets) throws HearthStoneException {
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        return String.format("%s: effect (mana cost: %d)",
                getName(), getManaCost());
    }
}
