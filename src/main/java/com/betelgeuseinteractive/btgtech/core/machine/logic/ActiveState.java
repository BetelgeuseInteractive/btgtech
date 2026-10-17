package com.betelgeuseinteractive.btgtech.core.machine.logic;

/**
 * The visible "active" state of a machine. Once changed, it stays unchanged for at least
 * {@value #MIN_TICKS_BETWEEN_CHANGES} ticks, which prevents flicker and repeated chunk re-renders on short pauses (ADR
 * 0047).
 */
public final class ActiveState {
    /** The minimum number of ticks between two visible changes. */
    public static final int MIN_TICKS_BETWEEN_CHANGES = 20;

    private boolean active;
    private long lastChange = -MIN_TICKS_BETWEEN_CHANGES;

    /**
     * Requests a state at the given game time and returns the visible state; the request takes effect once enough time
     * has passed since the last change.
     */
    public boolean update(boolean wanted, long gameTime) {
        if (wanted != active && gameTime - lastChange >= MIN_TICKS_BETWEEN_CHANGES) {
            active = wanted;
            lastChange = gameTime;
        }
        return active;
    }

    /** Returns the visible state. */
    public boolean isActive() {
        return active;
    }

    /** Sets the visible state without a delay, for example from the saved block state; the next change is allowed at once. */
    public void restore(boolean active) {
        this.active = active;
        this.lastChange = -MIN_TICKS_BETWEEN_CHANGES;
    }
}
