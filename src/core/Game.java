package core;

import core.players.Player;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import core.deck.Deck;
import core.exceptions.HearthStoneException;
import core.exceptions.InvalidActionException;
import players.RandomPlayer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {

    private UUID id;
    private Date startedAt;
    private Date finishedAt;
    private State currentState;
    private Collection<Player> players;

    private Player winner;

    public Game(Collection<Player> players) {
        this(UUID.randomUUID(), players);
    }

    public Game(UUID id, Collection<Player> players) {
        this.id = id;
        this.players = players;
    }

    public Game run() {
        this.startedAt = new Date();

        currentState = State.INITIAL;

        while (currentState.isDone()) {
            players.forEach((p) -> {
                try {
                    Action action = p.act(currentState);

                    if (action == null) {
                        throw new InvalidActionException("action cannot be null");
                    }

                    currentState = currentState.update(action);

                    if (currentState.isDone()) {
                        // TODO: check if this is suficient condition.
                        winner = p;
                    }

                } catch (HearthStoneException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            currentState = currentState.updateTurn();
        }

        this.finishedAt = new Date();
        return this;
    }

    public static Game random(Integer numberOfPlayers, Integer numberOfCardsForEachPlayer, Random randomState) {
        Collection<Player> players = new LinkedList<>();

        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new RandomPlayer(String.valueOf(i), Deck.random(numberOfCardsForEachPlayer, randomState)));
        }

        return new Game(players);
    }

    public static Game random(Integer numberOfPlayers, Integer numberOfCardsForEachPlayer, Integer seed) {
        return random(numberOfPlayers, numberOfCardsForEachPlayer, new Random(seed));
    }

    @Override
    public String toString() {
        return String.format("Game #%d's players:\n %s",
                id,
                String.join("\n ", players.stream()
                        .map(p -> String.format("#%s's deck:\n%s\n", p.getName(), p.getDeck()))
                        .toArray(size -> new String[size])));
    }

    public Date getStartedAt() {
        return (Date) this.startedAt.clone();
    }

    public Date getFinishedAt() {
        return (Date) this.finishedAt.clone();
    }

    public State getCurrentState() {
        return currentState;
    }

    public Player getWinner() {
        return winner;
    }
}
