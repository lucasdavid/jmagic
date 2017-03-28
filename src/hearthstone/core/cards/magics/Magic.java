package hearthstone.core.cards.magics;

import hearthstone.core.cards.Card;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public abstract class Magic extends Card {

    public Magic(String name, int manaCost) {
        super(name, manaCost);
    }

    public Magic(UUID id, String name, int manaCost) {
        super(id, name, manaCost);
    }
}
