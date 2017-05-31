package magic.core.actions;

import magic.core.ITargetable;
import magic.core.actions.validation.rules.cards.IsAlreadyAttached;
import magic.core.actions.validation.rules.players.CardsAreInAnyField;
import magic.core.actions.validation.rules.players.active.HasCardsInField;
import magic.core.cards.ICard;
import magic.core.cards.magics.attachments.IAttachable;
import magic.core.cards.magics.attachments.IAttachment;
import magic.core.states.State;
import magic.infrastructure.validation.rules.ValidationRule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static magic.infrastructure.validation.basic.Connectives.And;
import static magic.infrastructure.validation.basic.Connectives.Not;

/**
 * Attach Action.
 * <p>
 * Attach a card to one or more attachable targets.
 *
 * @author ldavid
 */
public class AttachAction extends Action {

    private final IAttachment card;
    private final List<IAttachable> targets;

    public AttachAction(IAttachment card, List<IAttachable> targets) {
        this.card = card;
        this.targets = targets;
    }

    @Override
    public State update(State state) {
        List<State.PlayerState> ps = state.playerStates();
        State.PlayerState a = state.activePlayerState();

        IAttachment validatedCard = (IAttachment) a.field.getValidated(card);
        List<IAttachable> validatedTargets = targets.stream()
            .map(t ->
                ps.stream()
                    .filter(p -> p.field.contains(t))
                    .map(p -> p.field.getValidated(t))
                    .findAny()
                    .get())
            .map(i -> (IAttachable) i)
            .collect(Collectors.toList());

        for (ITargetable t : validatedTargets) {
            State.PlayerState p = ps.stream()
                .filter(d -> d.field.contains((ICard) t))
                .findFirst()
                .get();

            ps.set(ps.indexOf(p),
                new State.PlayerState(p.player, p.life(), p.maxLife(),
                    p.deck, p.hand,
                    p.field.update(((IAttachable) t).attach(validatedCard)),
                    p.graveyard, p.attackers, p.blockers, p.playing)
            );
        }

        ps.set(ps.indexOf(a),
            new State.PlayerState(a.player, a.life(), a.maxLife(), a.deck,
                a.hand, a.field.remove(validatedCard), a.graveyard,
                a.attackers, a.blockers, a.playing));

        return new State(ps, state.turn, state.step, state.done,
            state.turnsPlayerIndex, state.activePlayerIndex,
            this, state);
    }

    @Override
    public ValidationRule validationRules() {
        return And(
            Not(new IsAlreadyAttached(card)),
            new HasCardsInField(card),
            new CardsAreInAnyField(new ArrayList<>(targets)));
    }

    public ICard card() {
        return card;
    }

    @Override
    public String toString() {
        return String.format("%s: %s -> %s", super.toString(), card, targets);
    }
}
