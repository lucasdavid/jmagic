package magic.players;

import magic.core.actions.Action;
import magic.core.actions.AttachAction;
import magic.core.cards.ICard;
import magic.core.cards.magics.attachments.Boost;
import magic.core.cards.magics.attachments.IAttachable;
import magic.core.cards.magics.attachments.IAttachment;
import magic.core.experts.IExpert;
import magic.core.experts.MulliganExpert;
import magic.core.states.State;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

/**
 * RandomPlayer.
 *
 * @author ldavid
 */
public class RandomPlayer extends NaivePlayer {

    private final Random random;

    public RandomPlayer() {
        this(new MulliganExpert());
    }

    public RandomPlayer(Random random) {
        this(new MulliganExpert(), random);
    }

    public RandomPlayer(String name) {
        this(name, new MulliganExpert(), new Random());
    }

    public RandomPlayer(IExpert mulliganExpert) {
        this(mulliganExpert, new Random());
    }

    public RandomPlayer(IExpert mulliganExpert, Random random) {
        super(mulliganExpert);
        this.random = random;
    }

    public RandomPlayer(String name, IExpert mulliganExpert, Random random) {
        super(name, mulliganExpert);
        this.random = random;
    }
}
