package magic.core.actions;

import magic.core.ITargetable;
import magic.core.actions.validation.rules.ActiveAndTurnsPlayersAreTheSame;
import magic.core.actions.validation.rules.ActivePlayerHasCardInField;
import magic.core.actions.validation.ValidationRule;
import magic.core.cards.Card;
import magic.core.states.State;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    protected Collection<ValidationRule> validationRules() {
        return List.of(
                new ActiveAndTurnsPlayersAreTheSame(),
                new ActivePlayerHasCardInField(card));
    }
}
