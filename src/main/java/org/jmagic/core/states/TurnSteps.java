package org.jmagic.core.states;

/**
 * Turn Steps.
 *
 * @author ldavid
 */
public enum TurnSteps {
    INITIAL_DRAWING(Phase.BOOTSTRAPING),
    UNTAP(Phase.BEGINNING), UPKEEP(Phase.BEGINNING), DRAW(Phase.BEGINNING),
    MAIN_1(Phase.PRE_COMBAT),
    BEGINNING_OF_COMBAT(Phase.COMBAT), DECLARE_ATTACKERS(Phase.COMBAT), DECLARE_BLOCKERS(Phase.COMBAT),
    COMBAT_DAMAGE(Phase.COMBAT), END_OF_COMBAT(Phase.COMBAT),
    MAIN_2(Phase.POST_COMBAT),
    END(Phase.END), CLEANUP(Phase.END);

    private Phase phase;

    TurnSteps(Phase phase) {
        this.phase = phase;
    }

    public Phase phase() {
        return this.phase;
    }

    public boolean in(Phase phase) {
        return this.phase == phase;
    }

    public TurnSteps next() {
        TurnSteps[] v = values();
        int currentOrdinal = (this.ordinal() + 1);

        if (currentOrdinal / v.length > 0 && currentOrdinal == 0) {
            // TurnSteps.INITIAL_DRAWING should only be visited once.
            currentOrdinal = 1;
        }

        return v[currentOrdinal % v.length];
    }

    public boolean isFirst() {
        return this == UNTAP;
    }

    public boolean isLast() {
        return this == CLEANUP;
    }

    public enum Phase {
        BOOTSTRAPING, BEGINNING, PRE_COMBAT, COMBAT, POST_COMBAT, END
    }
}
