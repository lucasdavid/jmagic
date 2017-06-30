package org.games.jmagic.actions.validation.rules.players.active;

import org.games.jmagic.players.Player;
import org.games.jmagic.core.cards.ICard;
import org.games.jmagic.core.cards.lands.BasicLands;
import org.games.jmagic.core.cards.lands.Land;
import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;

import java.util.Collection;


public class HasLandsToPlayIt extends ValidationRule {

    private final ICard card;
    private final Player player;

    public HasLandsToPlayIt(ICard card) {
        this(null, card);
    }

    public HasLandsToPlayIt(Player player, ICard card) {
        this.player = player;
        this.card = card;
    }

    @Override
    public void onValidate(State state) {
        Collection<BasicLands> cost = card.cost();
        State.PlayerState ps = player != null
            ? state.playerState(player)
            : state.activePlayerState();

        ps.field.cards().stream()
            .filter(c -> c instanceof Land && !((Land) c).tapped())
            .map(c -> ((Land) c).kind())
            .forEach(cost::remove);

        if (!cost.isEmpty()) {
            errors.add(String.format("%s doesn't have the correct combination of lands to play %s",
                ps.player, card));
        }
    }
}
