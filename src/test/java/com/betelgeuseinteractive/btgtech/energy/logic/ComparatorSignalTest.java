package com.betelgeuseinteractive.btgtech.energy.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ComparatorSignalTest {
    @Test
    void emptyGivesNoSignalAndFullGivesTheStrongest() {
        assertEquals(0, ComparatorSignal.of(0, 1_000_000));
        assertEquals(15, ComparatorSignal.of(1_000_000, 1_000_000));
    }

    @Test
    void anyStoredEnergyGivesASignal() {
        assertEquals(1, ComparatorSignal.of(1, 64_000_000));
    }

    @Test
    void theSignalFollowsTheFillLevelRoundedDown() {
        assertEquals(8, ComparatorSignal.of(500_000, 1_000_000));
        assertEquals(14, ComparatorSignal.of(999_999, 1_000_000));
    }

    @Test
    void largeAmountsDoNotOverflow() {
        assertEquals(14, ComparatorSignal.of(Integer.MAX_VALUE - 1L, Integer.MAX_VALUE));
    }

    @Test
    void aStoreWithoutCapacityGivesNoSignal() {
        assertEquals(0, ComparatorSignal.of(10, 0));
    }
}
