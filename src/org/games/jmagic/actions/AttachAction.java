package org.games.jmagic.actions;

import org.games.jmagic.actions.validation.rules.cards.IsAlreadyAttached;
import org.games.jmagic.actions.validation.rules.players.CardsAreInAnyField;
import org.games.jmagic.actions.validation.rules.players.active.HasCardsInField;
import org.games.jmagic.core.cards.ICard;
import org.games.jmagic.core.cards.attachments.IAttachable;
import org.games.jmagic.core.cards.attachments.IAttachment;
import org.games.jmagic.core.states.State;
import org.games.jmagic.infrastructure.ITargetable;
import org.games.jmagic.infrastructure.validation.rules.ValidationRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.games.jmagic.infrastructure.validation.basic.Connectives.And;
import static org.games.jmagic.infrastructure.validation.basic.Connectives.Not;

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
    public boolean equals(Object o) {
        try {
            AttachAction c = (AttachAction) o;
            return this == o || card.equals(c.card) && targets.equals(c.targets);
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 5 * hash + Objects.hashCode(card);
        hash = 11 * hash + Objects.hashCode(targets);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%s: %s -> %s", super.toString(), card, targets);
    }
}
