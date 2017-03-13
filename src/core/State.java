package core;

public class State {

    public static final State INITIAL = new State(0, false);

    private boolean done;
    private int turn;

    public State() {
        this(0, false);
    }

    public State(int turn, boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return this.done;
    }

    public State update(Action action) {
        return new State(turn, done);
    }

    public State updateTurn() {
        return new State(turn + 1, done);
    }

    public int getTurn() {
        return turn;
    }

    public State copy() {
        return new State(turn, done);
    }
}
