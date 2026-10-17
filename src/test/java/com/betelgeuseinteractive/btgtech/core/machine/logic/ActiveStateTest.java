package com.betelgeuseinteractive.btgtech.core.machine.logic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ActiveStateTest {
    private final ActiveState state = new ActiveState();

    @Test
    void theFirstChangeIsImmediate() {
        assertFalse(state.isActive());

        assertTrue(state.update(true, 0));
        assertTrue(state.isActive());
    }

    @Test
    void changesWaitForTheMinimumInterval() {
        state.update(true, 100);

        assertTrue(state.update(false, 100 + ActiveState.MIN_TICKS_BETWEEN_CHANGES - 1));
        assertFalse(state.update(false, 100 + ActiveState.MIN_TICKS_BETWEEN_CHANGES));
        assertFalse(state.update(true, 100 + ActiveState.MIN_TICKS_BETWEEN_CHANGES + 5));
    }

    @Test
    void requestsForTheCurrentStateDoNotRestartTheInterval() {
        state.update(true, 100);
        state.update(true, 115);

        assertFalse(state.update(false, 120));
    }

    @Test
    void restoreSetsTheStateAndAllowsTheNextChangeAtOnce() {
        state.update(false, 50);
        state.restore(true);

        assertTrue(state.isActive());
        assertFalse(state.update(false, 51));
    }
}
