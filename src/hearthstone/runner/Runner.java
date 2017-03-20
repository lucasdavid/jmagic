package hearthstone.runner;

import hearthstone.core.Game;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class Runner {

    public static final int NUMBER_OF_MATCHES = 1;
    public static final int NUMBER_OF_PLAYERS = 2;
    public static final int NUMBER_OF_CARDS_FOR_EACH_PLAYER = 10;
    public static final int SEED = 48;

    public static final Logger LOG = Logger.getLogger(Runner.class.getName());

    public static void main(String[] args) {
        new Runner(NUMBER_OF_MATCHES, NUMBER_OF_PLAYERS,
                NUMBER_OF_CARDS_FOR_EACH_PLAYER, SEED)
                .startUp()
                .run()
                .tearDown();
    }

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

    public Runner startUp() {
        return this;
    }

    public Runner tearDown() {
        return this;
    }

    public Runner run() {
        this.running = true;
        games = new ArrayList<>();
        Random randomState = new Random(seed);

        for (int matchId = 0; matchId < numberOfMatches; matchId++) {
            Game game = Game.random(numberOfPlayers, numberOfCardsForEachPlayer, randomState);
            games.add(game);

            LOG.info(String.format("Game #%d started", matchId));
            game.run();
            LOG.info(String.format("Game started at %s and finished at %s.\n",
                    game.getStartedAt(), game.getFinishedAt()));
        }

        this.running = false;

        return this;
    }

    public boolean isRunning() {
        return running;
    }

}