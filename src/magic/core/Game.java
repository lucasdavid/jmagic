package magic.core;

import magic.core.actions.Action;
import magic.core.actions.DiscardAction;
import magic.core.actions.DisqualifyAction;
import magic.core.actions.DrawAction;
import magic.core.actions.FinishGameAction;
import magic.core.actions.PassAction;
import magic.core.actions.PlayAction;
import magic.core.actions.UseAction;
import magic.core.cards.Cards;
import magic.core.exceptions.IllegalActionException;
import magic.core.exceptions.InvalidActionException;
import magic.core.exceptions.JMagicException;
import magic.infrastructure.collectors.CustomCollectors;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
            DiscardAction.class, DrawAction.class, PassAction.class, PlayAction.class,
            UseAction.class);

    private final boolean disqualifyOnInvalidAction;
    private final boolean disqualifyOnIllegalAction;
    private final UUID id;

    private Date startedAt;
    private Date finishedAt;
    private State currentState;
    private Collection<Player> players;
    private Collection<Player> winners;

    public Game(List<Player> players, List<Cards> playersCards,
                boolean disqualifyOnInvalidAction, boolean disqualifyOnIllegalAction) {
        this(UUID.randomUUID(), players, new State(players, playersCards),
                disqualifyOnInvalidAction, disqualifyOnIllegalAction);
    }

    public Game(List<Player> players, State initialState,
                boolean disqualifyOnInvalidAction, boolean disqualifyOnIllegalAction) {
        this(UUID.randomUUID(), players, initialState,
                disqualifyOnInvalidAction, disqualifyOnIllegalAction);
    }

    public Game(UUID id, List<Player> players, State initialState,
                boolean disqualifyOnInvalidAction, boolean disqualifyOnIllegalAction) {
        this.id = id;
        this.players = players;
        this.currentState = initialState;
        this.disqualifyOnInvalidAction = disqualifyOnInvalidAction;
        this.disqualifyOnIllegalAction = disqualifyOnIllegalAction;
    }

    public Game run() {
        this.startedAt = new Date();

        LOG.log(Level.INFO, "Initial state: {0}", currentState);

        while (!currentState.done) {
            for (Player player : players) {
                try {
                    Action action = player.act(currentState.playerViewModel(player));

                    if (action == null) {
                        continue;
                    }

                    if (!LEGAL_ACTIONS_FOR_PLAYERS.contains(action.getClass())) {
                        throw new IllegalActionException("action " + action
                                + "is not defined as legal by the game rules");
                    }

                    action.raiseForErrors(currentState, player);
                    currentState = action.update(currentState, player);
                    LOG.info(String.format("%s performed %s", player, action));

                } catch (InvalidActionException ex) {
                    // This player's attempted an invalid action.
                    LOG.log(Level.WARNING, null, ex);
                    passOrFinish(player, disqualifyOnInvalidAction);
                } catch (IllegalActionException ex) {
                    // This player's attempted to use an illegal action. Disqualify them.
                    LOG.log(Level.SEVERE, null, ex);
                    passOrFinish(player, disqualifyOnIllegalAction);
                } catch (Exception ex) {
                    // A very serious exception has been raised. Stops the game altogether.
                    LOG.log(Level.SEVERE, null, ex);
                    currentState = new FinishGameAction().update(currentState, player);
                }

                if (currentState.done) {
                    break;
                }
            }
        }

        winners = currentState.playerStates().stream()
                .filter(State.PlayerState::isAlive)
                .map(i -> i.player)
                .collect(CustomCollectors.toImmutableList());

        this.finishedAt = new Date();
        return this;
    }

    /**
     * @param player           player that may be disqualified and will pass.
     * @param disqualifyPlayer flag whether or not the player should be disqualified.
     */
    private void passOrFinish(Player player, boolean disqualifyPlayer) {
        if (disqualifyPlayer) {
            currentState = new DisqualifyAction().update(currentState, player);
        }

        try {
            currentState = new PassAction()
                    .raiseForErrors(currentState, player)
                    .update(currentState, player);
        } catch (JMagicException e) {
            currentState = new FinishGameAction().update(currentState, player);
        }
    }

    @Override
    public String toString() {
        return String.format("Game #%d's players: %s", id, players)
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
