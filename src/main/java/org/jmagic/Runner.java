package org.jmagic;

import org.jmagic.actions.*;
import org.jmagic.core.Game;
import org.jmagic.core.GameBuilder;
import org.jmagic.observers.*;
import org.jmagic.players.RandomPlayer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.LogManager;
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

    public static final int SEED = 42;

    public static final int MATCHES = 1;
    public static final int PLAYERS_INITIAL_LIFE = 20;
    public static final int CARDS = 40;
    public static final double PLAYER_ACT_TIMEOUT = 1.;

    public static final List<String> PLAYERS = List.of("Lucas", "Karen");
    public static final List<Observer> OBSERVERS = Arrays.asList(
        new HumanObserver(.5),
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
        new FinishIfLastPlayersAlive());

    private final int seed;
    private final int numberOfMatches;
    private final int numberOfCardsForEachPlayer;
    private final double playerActTimeout;
    private final List<String> players;
    private final List<Observer> observers;

    public Runner(int numberOfMatches, int numberOfCardsForEachPlayer,
                  double playerActTimeout, List<String> players, List<Observer> observers, int seed) {
        this.numberOfMatches = numberOfMatches;
        this.numberOfCardsForEachPlayer = numberOfCardsForEachPlayer;
        this.playerActTimeout = playerActTimeout;
        this.players = players;
        this.observers = observers;
        this.seed = seed;
    }

    public void run() {
        try {
            LogManager.getLogManager().readConfiguration(getClass()
                .getClassLoader()
                .getResourceAsStream("game-logging.properties"));
        } catch (IOException ignored) { }

        Random r = new Random(seed);

        GameBuilder gameBuilder = new GameBuilder(
            players.stream()
                .map(name -> new RandomPlayer(name, r))
                .collect(Collectors.toList()),
            PLAYERS_INITIAL_LIFE, numberOfCardsForEachPlayer, playerActTimeout, observers, r);

        IntStream.range(0, numberOfMatches).forEach(matchId -> {
            Game game = gameBuilder.build();

            LOG.info(String.format("Game #%d started", matchId));
            game.run();
            LOG.info(String.format("Game ran from %s to %s. Winners: %s\n",
                game.startedAt(), game.finishedAt(), game.winners()));
        });
    }

    public static void main(String[] args) {
        new Runner(MATCHES, CARDS, PLAYER_ACT_TIMEOUT, PLAYERS, OBSERVERS, SEED)
            .run();
    }
}
