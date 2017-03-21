package hearthstone.core;

import hearthstone.core.actions.Action;
import hearthstone.core.cards.Cards;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * State for a HearthStone Game.
 *
 * Holds information on the current environment of the game.
 *
 * @author ldavid
 */
public class State {

    public static class PlayerInfo {

        public static final int DEFAULT_INITIAL_LIFE = 20;

        public final int life;
        public final Player player;
        public final Cards deck, hand, field, graveyard;

        PlayerInfo(Player player, Cards deck) {
            this(player, DEFAULT_INITIAL_LIFE, deck, Cards.EMPTY, Cards.EMPTY, Cards.EMPTY);
        }

        PlayerInfo(PlayerInfo playerInfo) {
            this(playerInfo.player, playerInfo.life, playerInfo.deck,
                    playerInfo.hand, playerInfo.field, playerInfo.graveyard);
        }

        public PlayerInfo(Player player, int life, Cards deck, Cards hand, Cards field, Cards graveyard) {
            this.player = player;
            this.life = life;
            this.deck = deck;
            this.hand = hand;
            this.field = field;
            this.graveyard = graveyard;
        }

        @Override
        public String toString() {
            return String.format("%s {\n  life: %d,\n  hand: %s,\n  deck: %s\n}", player, life,
                    hand, deck);
        }
    }

    public final int turn;
    public final boolean done;
    public final State parent;
    public final int turnsCurrentPlayerId;
    public final Action actionThatLedToThisState;
    private final List<PlayerInfo> playerInfos;

    public State(State state) {
        this(state.playerInfos, state.turn, state.done, state.turnsCurrentPlayerId,
                state.actionThatLedToThisState, state.parent);
    }

    public State(State state, int turn) {
        this(state.playerInfos, turn, state.done, state.turnsCurrentPlayerId,
                state.actionThatLedToThisState, state.parent);
    }

    public State(State state, boolean done) {
        this(state.playerInfos, state.turn, done, state.turnsCurrentPlayerId,
                state.actionThatLedToThisState, state.parent);
    }

    public State(State state, int turnsCurrentPlayerId, Action actionThatLedToThisState) {
        this(state.playerInfos, state.turn, state.done, turnsCurrentPlayerId,
                actionThatLedToThisState, state);
    }

    public State(List<Player> players, List<Cards> playerCards) {
        List<PlayerInfo> info = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            info.add(new PlayerInfo(players.get(i), playerCards.get(i)));
        }

        this.turn = 0;
        this.done = false;
        this.turnsCurrentPlayerId = 0;
        this.playerInfos = info;
        this.parent = null;
        this.actionThatLedToThisState = null;
    }

    public State(List<PlayerInfo> playerInfos, int turn, boolean done,
            int turnsCurrentPlayerId, Action action, State parent) {
        this.turn = turn;
        this.done = done;
        this.turnsCurrentPlayerId = turnsCurrentPlayerId;
        this.playerInfos = playerInfos;
        this.parent = parent;
        this.actionThatLedToThisState = action;
    }

    public PlayerInfo turnsCurrentPlayerInfo() {
        return playerInfos.get(turnsCurrentPlayerId);
    }

    public List<PlayerInfo> getPlayerInfos() {
        return new ArrayList<>(playerInfos);
    }

    @Override
    public String toString() {
        return String.format("turn: %d, players:\n %s", turn,
                String.join("\n", playerInfos.stream().map(e -> e.toString()).collect(Collectors.toList())))
                + (done ? "(done)" : "");
    }
}
