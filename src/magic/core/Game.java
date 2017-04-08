package magic.core;

import magic.core.actions.Action;
import magic.core.actions.AdvanceGameAction;
import magic.core.actions.DiscardAction;
import magic.core.actions.DisqualifyAction;
import magic.core.actions.DrawAction;
import magic.core.actions.FinishGameAction;
import magic.core.actions.PlayAction;
import magic.core.actions.UntapAction;
import magic.core.actions.UseAction;
import magic.core.cards.Cards;
import magic.core.exceptions.IllegalActionException;
import magic.core.exceptions.InvalidActionException;
import magic.core.exceptions.JMagicException;
import magic.core.states.State;
import magic.core.states.TurnStep;
import magic.infrastructure.collectors.CustomCollectors;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Game.
 * <p>
 * Defines a Game (a box with players, winners and a state history) and
 * protects it using privacy.
 *
 * @author ldavid
 */
public class Game {

    public static final Logger LOG = Logger.getLogger(Game.class.getName());

    public final static Collection<Class<? extends Action>> LEGAL_ACTIONS_FOR_PLAYERS = Set.of(
        DiscardAction.class, DrawAction.class, AdvanceGameAction.class, PlayAction.class,
        UseAction.class);
    private final boolean disqualifyOnInvalidAction;
    private final boolean disqualifyOnIllegalAction;
    private final boolean disqualifyOnActTimeout;
    private long playerActTimeout;
    private Date startedAt;
    private Date finishedAt;
    private Collection<Player> players;
    private Collection<Player> winners;

    private State _currentState;
    private Player _activePlayer;

    public Game(List<Player> players, List<Cards> playersCards,
                long playerActTimeout, boolean disqualifyOnInvalidAction, boolean disqualifyOnIllegalAction,
                boolean disqualifyOnActTimeout) {
        this(players, new State(players, playersCards),
            playerActTimeout, disqualifyOnInvalidAction, disqualifyOnIllegalAction, disqualifyOnActTimeout);
    }

    public Game(List<Player> players, State initialState,
                long playerActTimeout, boolean disqualifyOnInvalidAction, boolean disqualifyOnIllegalAction,
                boolean disqualifyOnActTimeout) {
        this.players = players;
        this._currentState = initialState;
        this.playerActTimeout = playerActTimeout;
        this.disqualifyOnInvalidAction = disqualifyOnInvalidAction;
        this.disqualifyOnIllegalAction = disqualifyOnIllegalAction;
        this.disqualifyOnActTimeout = disqualifyOnActTimeout;
    }

    public Game run() {
        this.startedAt = new Date();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Action> askForPlayersAction = () -> _activePlayer.act(
            _currentState.playerViewModel(_activePlayer));

        LOG.log(Level.INFO, "Initial state: {0}", _currentState);

        while (!_currentState.done) {
            _activePlayer = _currentState.activePlayerState().player;

            try {
                if (_currentState.step == TurnStep.UNTAP &&
                    _currentState.activePlayerIndex == _currentState.turnsPlayerIndex) {
                    // Always un-tap cards automatically.
                    _currentState = new UntapAction(_activePlayer)
                        .raiseForErrors(_currentState)
                        .update(_currentState);
                }

                Action action = executor
                    .submit(askForPlayersAction)
                    .get(this.playerActTimeout, TimeUnit.MILLISECONDS);

                if (action == null) {
                    throw new InvalidActionException("action cannot be empty (try `AdvanceGameAction` if your intention is to perform no actions)");
                }

                if (!LEGAL_ACTIONS_FOR_PLAYERS.contains(action.getClass())) {
                    throw new IllegalActionException(String.format("action {%s} is illegal by the game rules", action));
                }

                _currentState = action
                    .raiseForErrors(_currentState)
                    .update(_currentState);

                LOG.info(String.format("%s performed %s", _activePlayer, action));

            } catch (InvalidActionException ex) {
                // This _activePlayer's attempted an invalid action.
                LOG.log(Level.WARNING, null, ex);
                passOrFinish(_activePlayer, disqualifyOnInvalidAction);
            } catch (IllegalActionException ex) {
                // This _activePlayer's attempted to use an illegal action. Disqualify them.
                LOG.log(Level.SEVERE, null, ex);
                passOrFinish(_activePlayer, disqualifyOnIllegalAction);
            } catch (TimeoutException ex) {
                LOG.log(Level.WARNING, null, ex);
                passOrFinish(_activePlayer, disqualifyOnActTimeout);
            } catch (Exception ex) {
                // A very serious exception has been raised. Stops the game altogether.
                LOG.log(Level.SEVERE, null, ex);
                _currentState = new FinishGameAction().update(_currentState);
            }
        }

        winners = _currentState.playerStates().stream()
            .filter(State.PlayerState::isAlive)
            .map(i -> i.player)
            .collect(CustomCollectors.toImmutableList());

        LOG.info("final game state: " + _currentState);

        this.finishedAt = new Date();
        return this;
    }

    /**
     * @param player           _activePlayer that may be disqualified and will pass.
     * @param disqualifyPlayer flag whether or not the _activePlayer should be disqualified.
     */
    private void passOrFinish(Player player, boolean disqualifyPlayer) {
        try {
            if (disqualifyPlayer) {
                _currentState = new DisqualifyAction(player).raiseForErrors(_currentState).update(_currentState);
            }

            _currentState = new AdvanceGameAction().raiseForErrors(_currentState).update(_currentState);
        } catch (JMagicException ex) {
            LOG.log(Level.SEVERE, null, ex);
            finish();
        }
    }

    private void finish() {
        try {
            _currentState = new FinishGameAction().raiseForErrors(_currentState).update(_currentState);
        } catch (JMagicException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
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
}
