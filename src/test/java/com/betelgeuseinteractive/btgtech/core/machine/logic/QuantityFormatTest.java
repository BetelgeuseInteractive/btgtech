package com.betelgeuseinteractive.btgtech.core.machine.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class QuantityFormatTest {
    @Test
    void amountsBelowOneThousandHaveNoPrefix() {
        assertEquals(new SiAmount("0", SiPrefix.NONE), QuantityFormat.si(0));
        assertEquals(new SiAmount("999", SiPrefix.NONE), QuantityFormat.si(999));
    }

    @Test
    void largerAmountsUseTheLargestPrefixWithOneDecimalDigit() {
        assertEquals(new SiAmount("1.0", SiPrefix.KILO), QuantityFormat.si(1_000));
        assertEquals(new SiAmount("12.3", SiPrefix.KILO), QuantityFormat.si(12_345));
        assertEquals(new SiAmount("3.4", SiPrefix.MEGA), QuantityFormat.si(3_456_789));
        assertEquals(new SiAmount("2.1", SiPrefix.GIGA), QuantityFormat.si(Integer.MAX_VALUE));
    }

    @Test
    void scalingRoundsDownSoThatALimitIsNeverShownEarly() {
        assertEquals(new SiAmount("999.9", SiPrefix.KILO), QuantityFormat.si(999_999));
        assertEquals(new SiAmount("1.0", SiPrefix.MEGA), QuantityFormat.si(1_099_999));
    }

    @Test
    void amountsBeyondTheLargestPrefixKeepCountingInIt() {
        assertEquals(new SiAmount("12345.6", SiPrefix.GIGA), QuantityFormat.si(12_345_678_901_234L));
    }

    @Test
    void exactAmountsGroupThousandsWithCommas() {
        assertEquals("0", QuantityFormat.exact(0));
        assertEquals("999", QuantityFormat.exact(999));
        assertEquals("12,345", QuantityFormat.exact(12_345));
        assertEquals("2,147,483,647", QuantityFormat.exact(Integer.MAX_VALUE));
    }

    @Test
    void negativeAmountsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> QuantityFormat.si(-1));
        assertThrows(IllegalArgumentException.class, () -> QuantityFormat.exact(-1));
    }
}
