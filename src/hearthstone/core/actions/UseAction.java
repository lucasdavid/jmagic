package hearthstone.core.actions;

import hearthstone.core.contracts.ITargetable;
import hearthstone.core.State;
import hearthstone.core.cards.Card;
import hearthstone.core.exceptions.HearthStoneException;
import hearthstone.core.exceptions.InvalidActionException;

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
        this(card, null);
    }

    public UseAction(Card card, List<ITargetable> targets) {
        this.card = card;
        this.targets = targets;
    }

    @Override
    public State update(State state) {
        State t = this.card.use(state, targets);

        // Override the state to make it look like
        // this action modified it instead of the card.
        return new State(t.getPlayersInfo(), t.turn, t.done, t.turnsCurrentPlayerId,
                this, state);
    }

    @Override
    public void validActionOrRaisesException(State state) throws HearthStoneException {
        if (!state.currentPlayerInfo().field.contains(card)) {
            throw new InvalidActionException("cannot use a card that's not on the field");
        }

        this.card.validUseOrRaisesException(state, targets);
    }
}
