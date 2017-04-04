package magic.runner;

import magic.core.Game;
import magic.core.GameBuilder;
import magic.core.Player;
import magic.infrastructure.collectors.CustomCollectors;
import magic.players.DrawerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * Game Runner.
 * <p>
 * Runs games and store their history inside an array.
 */
public class Runner {

    public static final int NUMBER_OF_MATCHES = 1;
    public static final int NUMBER_OF_PLAYERS = 2;
    public static final int NUMBER_OF_CARDS_FOR_EACH_PLAYER = 40;
    public static final int SEED = 48;
    public static final Logger LOG = Logger.getLogger(Runner.class.getName());
    private final int numberOfMatches;
    private final int numberOfPlayers;
    private final int numberOfCardsForEachPlayer;
    private final int seed;
    private ArrayList<Game> games;
    private boolean running;

    Runner(int numberOfMatches, int numberOfPlayers,
           int numberOfCardsForEachPlayer, int seed) {
        this.numberOfMatches = numberOfMatches;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfCardsForEachPlayer = numberOfCardsForEachPlayer;
        this.seed = seed;
    }

    public static void main(String[] args) {
        new Runner(NUMBER_OF_MATCHES, NUMBER_OF_PLAYERS,
                NUMBER_OF_CARDS_FOR_EACH_PLAYER, SEED)
                .startUp()
                .run()
                .tearDown();
    }

    public Runner startUp() {
        games = new ArrayList<>();
        return this;
    }

    public Runner tearDown() {
        games = null;
        return this;
    }

    public Runner run() {
        running = true;

        GameBuilder gb = new GameBuilder(
                IntStream
                        .range(0, numberOfPlayers)
                        .mapToObj(i -> new DrawerPlayer(Player.DEFAULT_PLAYER_NAMES[i % Player.DEFAULT_PLAYER_NAMES.length]))
                        .collect(CustomCollectors.toImmutableList()), numberOfCardsForEachPlayer,
                true, true, new Random(seed));

        IntStream.range(0, numberOfMatches).forEach(matchId -> {
            Game g = gb.build();

            games.add(g);

            LOG.info(String.format("Game #%d started", matchId));
            g.run();
            LOG.info(String.format("Game started at %s and finished at %s. Winners: %s\n",
                    g.startedAt(), g.finishedAt(), g.winners()));
        });

        running = false;
        return this;
    }

    public boolean running() {
        return running;
    }

    public List<Game> games() {
        return new ArrayList<>(games);
    }
}
