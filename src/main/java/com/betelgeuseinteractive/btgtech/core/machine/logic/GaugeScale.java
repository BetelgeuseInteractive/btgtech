package com.betelgeuseinteractive.btgtech.core.machine.logic;

/** Converts a value into the filled length of a gauge such as the energy bar or the progress arrow. */
public final class GaugeScale {
    private GaugeScale() {}

    /**
     * Returns how many of the gauge's pixels are filled. The length is rounded down, but any value above zero fills at
     * least one pixel, so a nearly empty gauge is still visibly not empty; only a full value fills the whole gauge.
     * Values outside {@code 0..max} are clamped, and a gauge with a maximum of zero or less is empty.
     */
    public static int filledLength(int value, int max, int length) {
        if (value <= 0 || max <= 0 || length <= 0) {
            return 0;
        }
        if (value >= max) {
            return length;
        }
        // Widened to long: an energy value times the gauge length can exceed the int range.
        return (int) Math.max(1, (long) value * length / max);
    }
}
