package com.betelgeuseinteractive.btgtech.core.machine.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GaugeScaleTest {
    @Test
    void lengthIsProportionalAndRoundedDown() {
        assertEquals(26, GaugeScale.filledLength(500, 1_000, 52));
        assertEquals(51, GaugeScale.filledLength(999, 1_000, 52));
    }

    @Test
    void onlyAFullValueFillsTheWholeGauge() {
        assertEquals(52, GaugeScale.filledLength(1_000, 1_000, 52));
        assertEquals(51, GaugeScale.filledLength(Integer.MAX_VALUE - 1, Integer.MAX_VALUE, 52));
    }

    @Test
    void anyValueAboveZeroFillsAtLeastOnePixel() {
        assertEquals(1, GaugeScale.filledLength(1, Integer.MAX_VALUE, 52));
    }

    @Test
    void valuesOutsideTheRangeAreClamped() {
        assertEquals(0, GaugeScale.filledLength(-5, 1_000, 52));
        assertEquals(52, GaugeScale.filledLength(5_000, 1_000, 52));
    }

    @Test
    void aGaugeWithoutMaximumIsEmpty() {
        assertEquals(0, GaugeScale.filledLength(10, 0, 52));
    }
}
