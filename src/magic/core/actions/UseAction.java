package magic.core.actions;

import magic.core.ITargetable;
import magic.infrastructure.validation.rules.ValidationRule;
import magic.core.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import magic.core.actions.validation.rules.players.active.HasCardInField;
import magic.core.cards.Card;
import magic.core.states.State;

import java.util.Collections;
import java.util.List;

import static magic.infrastructure.validation.connectives.Connectives.And;

/**
 * Use Action.
 *
 * @author ldavid
 */
public class UseAction extends Action {

    private final Card card;
    private final List<ITargetable> targets;

    public UseAction(Card card) {
        this(card, Collections.emptyList());
    }

    public UseAction(Card card, List<ITargetable> targets) {
        this.card = card;
        this.targets = Collections.unmodifiableList(targets);
    }

    @Override
    public State update(State state) {
        throw new UnsupportedOperationException();
//        State t = this.card.use(state, targets);
//
//        // Override the state to make it look like
//        // this action modified it instead of the card.
//        return new State(t.playersState(), t.turn, t.done, t.turnsPlayerIndex,
//                this, state);
    }

    @Override
    public ValidationRule validationRules() {
        return this.card.validationRules();
    }
}
