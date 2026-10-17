package com.betelgeuseinteractive.btgtech.core.machine.logic;

/**
 * Progress of a machine's current operation, in ticks (see {@code docs/architecture/machines.md}, section 3).
 *
 * <p>An operation runs from {@link #start(int)} until {@link #advance()} reports completion; the owner then starts the
 * next operation or calls {@link #reset()}.
 */
public final class ProcessingProgress {
    private int progress;
    private int duration;

    /**
     * Starts an operation of the given length; any current progress is discarded.
     *
     * @throws IllegalArgumentException if the duration is not positive
     */
    public void start(int duration) {
        if (duration < 1) {
            throw new IllegalArgumentException("Duration must be positive: " + duration);
        }
        this.duration = duration;
        this.progress = 0;
    }

    /** Advances the running operation by one tick and returns whether it is complete; does nothing when idle. */
    public boolean advance() {
        if (!isRunning()) {
            return false;
        }
        if (progress < duration) {
            progress++;
        }
        return progress == duration;
    }

    /** Returns whether an operation has been started and not reset. */
    public boolean isRunning() {
        return duration > 0;
    }

    /** Ends the current operation. */
    public void reset() {
        progress = 0;
        duration = 0;
    }

    /** Returns the ticks done in the current operation. */
    public int progress() {
        return progress;
    }

    /** Returns the length of the current operation in ticks, or 0 when idle. */
    public int duration() {
        return duration;
    }

    /** Restores saved values; inconsistent values (negative, or progress beyond the duration) are corrected. */
    public void restore(int progress, int duration) {
        this.duration = Math.max(0, duration);
        this.progress = Math.clamp(progress, 0, this.duration);
    }
}
