package magic.core.actions;

import magic.core.Player;
import magic.core.actions.validation.ValidationRule;
import magic.core.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import magic.core.actions.validation.rules.players.active.HasNotAlreadyUntappedInThisTurn;
import magic.core.actions.validation.rules.TurnsStepIs;
import magic.core.cards.Cards;
import magic.core.cards.ITappable;
import magic.core.states.State;
import magic.core.states.TurnStep;

import java.util.List;
import java.util.stream.Collectors;

import static magic.core.actions.validation.ValidationRules.And;

/**
 * Untap Action.
 * <p>
 * Un-tap all tapped cards of player.
 *
 * @author ldavid
 */
public class UntapAction extends Action {

    private final Player player;

    public UntapAction(Player player) {
        this.player = player;
    }

    @Override
    public State update(State state) {
        List<State.PlayerState> players = state.playerStates().stream()
            .map(s -> s.isAlive() && s.player.equals(this.player)
                ? new State.PlayerState(s.player, s.life(), s.maxLife(),
                s.deck, s.hand,
                new Cards(s.field.cards().stream()
                    .map(c -> c instanceof ITappable ? ((ITappable) c).untap() : c)
                    .collect(Collectors.toList())),
                s.graveyard, s.playing)
                : s)
            .collect(Collectors.toList());
        return new State(players, state.turn, state.step, state.done,
            state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    protected ValidationRule validationRules() {
        return And(
            new TurnsStepIs(TurnStep.UNTAP),
            new ActiveAndTurnsPlayersAreTheSame(),
            new HasNotAlreadyUntappedInThisTurn());
    }
}
