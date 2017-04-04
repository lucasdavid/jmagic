package magic.core.actions;

import magic.core.Player;
import magic.core.State;
import magic.core.State.PlayerState;
import magic.core.actions.validation.ActorIsCurrentPlayer;
import magic.core.actions.validation.PlayerHasCardInHand;
import magic.core.actions.validation.PlayerHasLandsToPlay;
import magic.core.actions.validation.ValidationRule;
import magic.core.cards.Card;
import magic.core.cards.Cards;
import magic.core.cards.lands.Land;
import magic.core.contracts.cards.ICard;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Play Action.
 * <p>
 * Play a card from the player's hand.
 *
 * @author ldavid
 */
public class PlayAction extends Action {

    private final Card card;

    public PlayAction(Card card) {
        this.card = card;
    }

    @Override
    public State update(State state, Player actor) {
        List<PlayerState> playerStates = state.playerStates();
        State.PlayerState p = playerStates.remove(state.turnsCurrentPlayerIndex);
        List<ICard> hand = p.hand.cards();
        List<ICard> field = p.field.cards();

        ICard validCard = hand.remove(hand.indexOf(card));
        field.add(validCard);

        List<ICard> landsToUse = field.stream()
                .filter(c -> c instanceof Land && !((Land) c).used())
                .map(c -> (Land) c)
                .map(l -> new Land(l.id(), l.kind(), true))
                .collect(Collectors.toList());

        field.removeAll(landsToUse);
        field.addAll(landsToUse);

        p = new State.PlayerState(p.player, p.life(), p.maxLife(),
                p.deck, new Cards(hand), new Cards(field), p.graveyard);
        playerStates.add(state.turnsCurrentPlayerIndex, p);

        return new State(playerStates, state.turn, state.done, state.turnsCurrentPlayerIndex,
                this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return List.of(
                new ActorIsCurrentPlayer(),
                new PlayerHasCardInHand(card),
                new PlayerHasLandsToPlay(card));
    }
}
