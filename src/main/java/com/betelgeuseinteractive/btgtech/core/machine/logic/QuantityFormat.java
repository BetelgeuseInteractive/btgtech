package com.betelgeuseinteractive.btgtech.core.machine.logic;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Formats amounts for screens and tooltips (gui.md, section 5). Like vanilla, numbers look the same in every language:
 * a point before the decimal digit and commas between groups of thousands.
 */
public final class QuantityFormat {
    private static final SiPrefix[] LARGEST_FIRST = {SiPrefix.GIGA, SiPrefix.MEGA, SiPrefix.KILO};

    private QuantityFormat() {}

    /**
     * Scales the amount to the largest prefix it reaches and keeps one decimal digit, rounded down so that a value
     * just below a limit never shows the limit: 999 stays {@code 999}, 12,345 becomes {@code 12.3} kilo and 999,999
     * becomes {@code 999.9} kilo. Amounts beyond the largest prefix keep counting in it.
     *
     * @throws IllegalArgumentException if the amount is negative
     */
    public static SiAmount si(long amount) {
        requireNotNegative(amount);
        for (SiPrefix prefix : LARGEST_FIRST) {
            if (amount >= prefix.factor()) {
                long tenths = amount / (prefix.factor() / 10);
                return new SiAmount(tenths / 10 + "." + tenths % 10, prefix);
            }
        }
        return new SiAmount(Long.toString(amount), SiPrefix.NONE);
    }

    /**
     * Returns the exact amount with commas between groups of thousands, for example {@code 12,345}.
     *
     * @throws IllegalArgumentException if the amount is negative
     */
    public static String exact(long amount) {
        requireNotNegative(amount);
        // A new instance per call: NumberFormat is not thread-safe, and tooltips are built rarely.
        return NumberFormat.getIntegerInstance(Locale.US).format(amount);
    }

    private static void requireNotNegative(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must not be negative: " + amount);
        }
    }
}
