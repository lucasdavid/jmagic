package magic.core.cards.lands;

import magic.core.states.State;
import magic.core.cards.Card;
import magic.core.cards.ICard;
import magic.core.ITargetable;
import magic.core.exceptions.JMagicException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Land Card.
 *
 * @author ldavid
 */
public class Land extends Card {

    private final BasicLands kind;
    private final boolean used;

    public Land(BasicLands kind) {
        this(kind, false);
    }

    public Land(BasicLands kind, boolean used) {
        this(UUID.randomUUID(), kind, used);
    }

    public Land(UUID id, BasicLands kind, boolean used) {
        super(id, kind.name().toLowerCase(), Collections.emptyList());
        this.kind = kind;
        this.used = used;
    }

    public BasicLands kind() {
        return kind;
    }

    @Override
    public State use(State state, List<ITargetable> targets) {
        return null;
    }

    @Override
    public void raiseForErrors(State state, List<ITargetable> targets) throws JMagicException {

    }

    public boolean used() {
        return used;
    }

    @Override
    public ICard duplicate() {
        return new Land(kind, used);
    }
}
