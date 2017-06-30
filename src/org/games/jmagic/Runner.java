package magic;

import org.games.jmagic.core.Game;
import org.games.jmagic.core.GameBuilder;
import org.games.jmagic.actions.AdvanceGameAction;
import org.games.jmagic.actions.AttachAction;
import org.games.jmagic.actions.ComputeDamageAction;
import org.games.jmagic.actions.DeclareAttackersAction;
import org.games.jmagic.actions.DeclareBlockersAction;
import org.games.jmagic.actions.DiscardAction;
import org.games.jmagic.actions.DrawAction;
import org.games.jmagic.actions.InitialDrawAction;
import org.games.jmagic.actions.PlayAction;
import org.games.jmagic.actions.UntapAction;
import org.games.jmagic.observers.LooseIfDrawingFromEmptyDeck;
import org.games.jmagic.observers.LooseOnActTimeout;
import org.games.jmagic.observers.LooseOnIllegalActionAttempt;
import org.games.jmagic.observers.LooseOnInvalidActionAttempt;
import org.games.jmagic.observers.LooseOnNullAction;
import org.games.jmagic.observers.Observer;
import org.games.jmagic.observers.PassOrFinishIfLost;
import org.games.jmagic.observers.WinIfLastPlayerAlive;
import org.games.jmagic.players.RandomPlayer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Game Runner.
 * <p>
 * Runs games and store their history inside an array.
 */
public class Runner {

    public static final Logger LOG = Logger.getLogger(Runner.class.getName());

    public static final int SEED = 48;
    public static final int N_MATCHES = 1;
    public static final int N_PLAYERS = 2;
    public static final int N_CARDS = 40;
    public static final long PLAYER_ACT_TIMEOUT = 1000;
    public static final List<Observer> OBSERVERS = Arrays.asList(
        new LooseIfDrawingFromEmptyDeck(),
        new LooseOnNullAction(),
        new LooseOnActTimeout(PLAYER_ACT_TIMEOUT),
        new LooseOnInvalidActionAttempt(),
        new LooseOnIllegalActionAttempt(
            DiscardAction.class,
            DrawAction.class,
            AdvanceGameAction.class,
            PlayAction.class,
            AttachAction.class,
            UntapAction.class,
            InitialDrawAction.class,
            DeclareAttackersAction.class,
            DeclareBlockersAction.class,
            ComputeDamageAction.class),
        new PassOrFinishIfLost(),
        new WinIfLastPlayerAlive());

    private final int seed;
    private final int numberOfMatches;
    private final int numberOfPlayers;
    private final int numberOfCardsForEachPlayer;
    private final long playerActTimeout;
    private final List<Observer> observers;

    private Runner(int numberOfMatches, int numberOfPlayers, int numberOfCardsForEachPlayer,
                   long playerActTimeout, List<Observer> observers, int seed) {
        this.numberOfMatches = numberOfMatches;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfCardsForEachPlayer = numberOfCardsForEachPlayer;
        this.playerActTimeout = playerActTimeout;
        this.observers = observers;
        this.seed = seed;
    }

    public static void main(String[] args) {
        new Runner(N_MATCHES, N_PLAYERS, N_CARDS, PLAYER_ACT_TIMEOUT, OBSERVERS, SEED)
            .run();
    }

    private void run() {
        Random random = new Random(seed);

        GameBuilder gameBuilder = new GameBuilder(
            IntStream.range(0, numberOfPlayers)
                .mapToObj(i -> new RandomPlayer(random))
                .collect(Collectors.toList()),
            numberOfCardsForEachPlayer, playerActTimeout, observers, random);

        IntStream.range(0, numberOfMatches).forEach(matchId -> {
            Game game = gameBuilder.build();

            LOG.info(String.format("Game #%d started", matchId));
            game.run();
            LOG.info(String.format("Game ran from %s to %s. Winners: %s\n",
                game.startedAt(), game.finishedAt(), game.winners()));
        });
    }
}
