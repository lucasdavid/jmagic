package magic.runner;

import magic.core.Game;
import magic.core.GameBuilder;
import magic.core.rules.LooseIfNullAction;
import magic.core.rules.MagicRule;
import magic.core.rules.LooseIfDrawingFromEmptyDeck;
import magic.core.rules.WinIfOnlyPlayerAlive;
import magic.players.RandomPlayer;

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

    public static final int SEED = 48;
    public static final int NUMBER_OF_MATCHES = 1;
    public static final int NUMBER_OF_PLAYERS = 2;
    public static final int NUMBER_OF_CARDS_FOR_EACH_PLAYER = 40;
    public static final long PLAYER_ACT_TIMEOUT = 1000;
    public static final boolean DISQUALIFY_ON_INVALID_ACTION = true;
    public static final boolean DISQUALIFY_ON_ILLEGAL_ACTION = true;
    public static final boolean DISQUALIFY_ON_ACT_TIMEOUT = true;
    private static final List<MagicRule> RULES = List.of(
        new LooseIfDrawingFromEmptyDeck(),
        new LooseIfNullAction(),
        new WinIfOnlyPlayerAlive());

    public static final Logger LOG = Logger.getLogger(Runner.class.getName());

    private final int numberOfMatches;
    private final int numberOfPlayers;
    private final int numberOfCardsForEachPlayer;
    private final long playerActTime;
    private final boolean disqualifyOnInvalidAction;
    private final boolean disqualifyOnIllegalAction;
    private final boolean disqualifyOnActTimeout;
    private final List<MagicRule> rules;
    private final int seed;

    private Runner(int numberOfMatches, int numberOfPlayers,
                   int numberOfCardsForEachPlayer, long playerActTime,
                   boolean disqualifyOnInvalidAction, boolean disqualifyOnIllegalAction,
                   boolean disqualifyOnActTimeout, List<MagicRule> rules, int seed) {
        this.numberOfMatches = numberOfMatches;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfCardsForEachPlayer = numberOfCardsForEachPlayer;
        this.playerActTime = playerActTime;
        this.disqualifyOnInvalidAction = disqualifyOnInvalidAction;
        this.disqualifyOnIllegalAction = disqualifyOnIllegalAction;
        this.disqualifyOnActTimeout = disqualifyOnActTimeout;
        this.rules = rules;
        this.seed = seed;
    }

    public static void main(String[] args) {
        new Runner(NUMBER_OF_MATCHES, NUMBER_OF_PLAYERS,
            NUMBER_OF_CARDS_FOR_EACH_PLAYER, PLAYER_ACT_TIMEOUT,
            DISQUALIFY_ON_INVALID_ACTION, DISQUALIFY_ON_ILLEGAL_ACTION,
            DISQUALIFY_ON_ACT_TIMEOUT, RULES, SEED)
            .run();
    }

    private void run() {
        GameBuilder gb = new GameBuilder(
            IntStream
                .range(0, numberOfPlayers)
                .mapToObj(i -> new RandomPlayer())
                .collect(Collectors.toList()),
            numberOfCardsForEachPlayer,
            playerActTime,
            disqualifyOnInvalidAction, disqualifyOnIllegalAction, disqualifyOnActTimeout,
            rules, new Random(seed));

        IntStream.range(0, numberOfMatches).forEach(matchId -> {
            Game g = gb.build();

            LOG.info(String.format("Game #%d started", matchId));
            g.run();
            LOG.info(String.format("Game started at %s and finished at %s. Winners: %s\n",
                g.startedAt(), g.finishedAt(), g.winners()));
        });
    }
}
