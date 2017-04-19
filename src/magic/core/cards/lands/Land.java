package magic.core.cards.lands;

import magic.core.ITargetable;
import magic.core.cards.Card;
import magic.core.cards.ICard;
import magic.core.cards.ITappable;
import magic.core.exceptions.JMagicException;
import magic.core.states.State;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Land Card.
 *
 * @author ldavid
 */
public class Land extends Card implements ITappable {

    private final BasicLands kind;
    private final boolean tapped;

    public Land(BasicLands kind) {
        this(kind, false);
    }

    public Land(BasicLands kind, boolean tapped) {
        this(UUID.randomUUID(), kind, tapped);
    }

    public Land(UUID id, BasicLands kind, boolean tapped) {
        super(id, kind.name().toLowerCase(), Collections.emptyList());
        this.kind = kind;
        this.tapped = tapped;
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

    @Override
    public ITappable tap() {
        return new Land(id(), kind, true);
    }

    @Override
    public ITappable untap() {
        return new Land(id(), kind, false);
    }

    @Override
    public boolean tapped() {
        return tapped;
    }

    @Override
    public ICard duplicate() {
        return new Land(kind, tapped);
    }

    @Override
    public String toString(boolean detailed) {
        return super.toString(false);
    }
}
