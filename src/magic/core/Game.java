package magic.core;

import magic.core.actions.Action;
import magic.core.actions.AdvanceGameAction;
import magic.core.cards.Cards;
import magic.core.observers.LooseOnNullAction;
import magic.core.observers.LooseOnInvalidActionAttempt;
import magic.core.observers.Observer;
import magic.core.observers.PassOrFinishIfLost;
import magic.core.observers.WinIfLastPlayerAlive;
import magic.core.states.State;
import magic.infrastructure.collectors.CustomCollectors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Game.
 * <p>
 * Defines a Game (a box with players, winners and a state history), keeps the
 * game running and protects its integrity using privacy and view models.
 *
 * @author ldavid
 */
public class Game {

    public static final Logger LOG = Logger.getLogger(Game.class.getName());

    private final long playerActTimeout;
    private final List<Observer> observers;
    private final List<Player> players;
    private Collection<Player> winners;
    private Date startedAt;
    private Date finishedAt;

    private State _currentState;
    private Player _activePlayer;

    public Game(List<Player> players, List<Cards> playersCards, long playerActTimeout, List<Observer> observers) {
        this(players, new State(players, playersCards), playerActTimeout, observers);
    }

    public Game(List<Player> players, State initialState, long playerActTimeout,
                List<Observer> observers) {
        this.players = players;
        this._currentState = initialState;
        this.playerActTimeout = playerActTimeout;

        observers = new ArrayList<>(observers);

        if (observers.stream().noneMatch(o -> o instanceof LooseOnNullAction)) {
            observers.add(new LooseOnNullAction());
        }

        if (observers.stream().noneMatch(o -> o instanceof LooseOnInvalidActionAttempt)) {
            observers.add(new LooseOnInvalidActionAttempt());
        }

        // Place `PassOrFinishIfLost` and `WinIfLastPlayerAlive` as the observers.
        // This is important to the game logic, which must first kill players and then
        // check whether or not they are alive.
        for (Class<? extends Observer> c : List.of(PassOrFinishIfLost.class, WinIfLastPlayerAlive.class)) {
            try {
                Observer o = observers.stream()
                    .filter(c::isInstance).findFirst().orElse(c.getConstructor().newInstance());
                observers.remove(o);
                observers.add(o);
            } catch (Exception ignored) {
            }
        }

        this.observers = Collections.unmodifiableList(observers);
    }

    public Game run() {
        this.startedAt = new Date();

        ExecutorService exec = Executors.newSingleThreadExecutor();
        Callable<Action> actRoutine = () -> _activePlayer.act(_currentState.playerViewModel(_activePlayer));

        LOG.log(Level.INFO, "Initial state: {0}", _currentState);

        while (!_currentState.done) {
            _activePlayer = _currentState.activePlayerState().player;

            for (Observer o : observers) _currentState = o.beforePlayerAct(_currentState);

            // One of the observers ended the game.
            if (_currentState.done) break;
            // One of the observers decided this player should pass.
            // This usually happens when the player attempts an invalid action.
            if (!_currentState.activePlayerState().player.equals(_activePlayer)) continue;

            Action action = null;
            Future<Action> f = null;

            long actStartedAt = System.currentTimeMillis();

            try {
                f = exec.submit(actRoutine);
                action = this.playerActTimeout <= 0
                    ? f.get()
                    : f.get(this.playerActTimeout, TimeUnit.MILLISECONDS);
            } catch (TimeoutException | InterruptedException ignored) {
            } catch (ExecutionException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            long actEndedAt = System.currentTimeMillis();
            f.cancel(true);

            for (Observer o : observers)
                _currentState = o.afterPlayerAct(_currentState, action, actStartedAt, actEndedAt);

            if (_currentState.done) break;
            if (!_currentState.activePlayerState().player.equals(_activePlayer)) continue;

            _currentState = action.update(_currentState);

            if (!AdvanceGameAction.class.isInstance(action)) {
                LOG.info(String.format("%s performed %s", _activePlayer, action));
            }
        }

        winners = _currentState.playerStates().stream()
            .filter(State.PlayerState::isAlive)
            .map(i -> i.player)
            .collect(CustomCollectors.toImmutableList());

        LOG.info("final game state: " + _currentState);

        exec.shutdown();
        this.finishedAt = new Date();
        return this;
    }

    @Override
    public String toString() {
        return String.format("Game's players: %s", players)
            + (winners != null && !winners.isEmpty()
            ? String.format(" winners: %s", winners)
            : "");
    }

    public Date startedAt() {
        return (Date) this.startedAt.clone();
    }

    public Date finishedAt() {
        return (Date) this.finishedAt.clone();
    }

    public Collection<Player> winners() {
        return winners;
    }

    public List<Observer> observers() {
        return observers;
    }
}
