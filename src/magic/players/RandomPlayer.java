package magic.players;

import magic.core.experts.IExpert;
import magic.core.experts.MulliganExpert;

/**
 * RandomPlayer.
 *
 * @author ldavid
 */
public class RandomPlayer extends NaivePlayer {

    public RandomPlayer() {
        this(new MulliganExpert());
    }

    public RandomPlayer(String name) {
        this(name, new MulliganExpert());
    }

    public RandomPlayer(IExpert mulliganExpert) {
        super(mulliganExpert);
    }

    public RandomPlayer(String name, IExpert mulliganExpert) {
        super(name, mulliganExpert);
    }
}
