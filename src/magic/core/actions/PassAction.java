package magic.core.actions;

import magic.core.Player;
import magic.core.State;
import magic.core.actions.validation.ActorIsCurrentPlayer;
import magic.core.actions.validation.AnotherPersonIsAlive;
import magic.core.actions.validation.HasAtLeastNPeopleAlive;
import magic.core.actions.validation.PlayerHasLessThan8CardsInTheirHand;
import magic.core.actions.validation.ValidationRule;
import magic.core.cards.Cards;
import magic.core.contracts.cards.ITappable;
import magic.infrastructure.collectors.CustomCollectors;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Pass Action.
 * <p>
 * Action used by a player to pass their turn.
 *
 * @author ldavid
 */
public class PassAction extends Action {

    @Override
    public State update(State state, Player actor) {
        int index = state.turnsCurrentPlayerIndex,
                turn = state.turn;

        List<State.PlayerState> players = state.playerStates();

        do if (++index == state.playerStates().size()) {
            // All players were analyzed. Turn it over and un-tap all tappable cards.
            index = 0;
            turn += 1;
            players = players.stream()
                    .map(s -> new State.PlayerState(s.player, s.life(), s.maxLife(),
                            s.deck, s.hand,
                            new Cards(s.field.cards().stream()
                                    .filter(c -> c instanceof ITappable)
                                    .map(c -> ((ITappable) c).untap())
                                    .collect(Collectors.toList())),
                            s.graveyard, s.playing))
                    .collect(CustomCollectors.toImmutableList());
        } while (!state.playerState(index).isAlive());

        return new State(players, turn, state.done, index, this, state);
    }

    @Override
    protected Collection<ValidationRule> validationRules() {
        return List.of(
                new ActorIsCurrentPlayer(),
                new PlayerHasLessThan8CardsInTheirHand(),
                new AnotherPersonIsAlive(),
                new HasAtLeastNPeopleAlive(2));
    }
}
