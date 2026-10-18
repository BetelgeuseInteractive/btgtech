package com.betelgeuseinteractive.btgtech.energy.logic;

/** The comparator output of an energy store, proportional to its fill level (ADR 0039), as vanilla containers do it. */
public final class ComparatorSignal {
    /** The strongest redstone signal. */
    public static final int MAX = 15;

    private ComparatorSignal() {}

    /**
     * Returns 0 when empty, 15 when full, and in between 1 plus the fill level scaled to 0..14, rounded down, so that
     * any stored energy gives a signal.
     */
    public static int of(long stored, long capacity) {
        if (stored <= 0 || capacity <= 0) {
            return 0;
        }
        if (stored >= capacity) {
            return MAX;
        }
        return 1 + (int) (stored * (MAX - 1) / capacity);
    }
}
