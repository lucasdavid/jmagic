package org.games.jmagic.core.cards.lands;

import org.games.jmagic.core.cards.Card;
import org.games.jmagic.core.cards.ICard;
import org.games.jmagic.core.cards.ITappable;
import org.games.jmagic.core.cards.Properties;

import java.util.Collection;
import java.util.Collections;
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
        this(id, kind, Collections.emptyList(), tapped);
    }

    public Land(UUID id, BasicLands kind, Collection<Properties> properties, boolean tapped) {
        super(id, kind.name().toLowerCase(), properties, Collections.emptyList());
        this.kind = kind;
        this.tapped = tapped;
    }

    public BasicLands kind() {
        return kind;
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
