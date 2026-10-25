package com.betelgeuseinteractive.btgtech.energy.logic;

/**
 * The fuel a generator is burning, counted in energy: a fuel item holds its burn time times the energy per burn tick,
 * the same on every tier (ADR 0043), and each tick releases at most the tier's output. Higher tiers therefore burn the
 * same fuel faster, never more efficiently. Nothing burns while the generator has no room for the energy.
 */
public final class FuelBurner {
    private int remaining;
    private int total;

    /** Returns whether fuel is burning. */
    public boolean isBurning() {
        return remaining > 0;
    }

    /** Returns the energy of the current fuel that is not released yet. */
    public int remaining() {
        return remaining;
    }

    /** Returns the energy of the current fuel when it started burning, or 0 when none burns. */
    public int total() {
        return total;
    }

    /**
     * Starts burning a fuel holding the given energy.
     *
     * @throws IllegalArgumentException if the energy is not positive
     * @throws IllegalStateException if fuel is still burning
     */
    public void start(int energy) {
        if (energy < 1) {
            throw new IllegalArgumentException("Fuel energy must be positive: " + energy);
        }
        if (isBurning()) {
            throw new IllegalStateException("Fuel is still burning");
        }
        remaining = energy;
        total = energy;
    }

    /**
     * Releases the energy of one tick: at most the output, the remaining fuel energy and the free room. Returns the
     * energy released; when the fuel is used up, the burner is ready for the next one.
     */
    public int burn(int output, int room) {
        int released = Math.max(0, Math.min(Math.min(output, room), remaining));
        remaining -= released;
        if (remaining == 0) {
            total = 0;
        }
        return released;
    }

    /** Restores a saved state; values out of range are clamped. */
    public void restore(int remaining, int total) {
        this.total = Math.max(0, total);
        this.remaining = Math.clamp(remaining, 0, this.total);
        if (this.remaining == 0) {
            this.total = 0;
        }
    }
}
