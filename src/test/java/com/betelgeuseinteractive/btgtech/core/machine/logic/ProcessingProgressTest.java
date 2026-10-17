package com.betelgeuseinteractive.btgtech.core.machine.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProcessingProgressTest {
    private final ProcessingProgress progress = new ProcessingProgress();

    @Test
    void startsIdle() {
        assertFalse(progress.isRunning());
        assertEquals(0, progress.progress());
        assertEquals(0, progress.duration());
        assertFalse(progress.advance());
    }

    @Test
    void advanceCompletesAfterTheDuration() {
        progress.start(3);

        assertTrue(progress.isRunning());
        assertFalse(progress.advance());
        assertFalse(progress.advance());
        assertTrue(progress.advance());
        assertEquals(3, progress.progress());
        assertTrue(progress.advance(), "progress stays complete until the next start or reset");
        assertEquals(3, progress.progress());
    }

    @Test
    void startDiscardsCurrentProgressAndRejectsNonPositiveDurations() {
        progress.start(5);
        progress.advance();

        progress.start(2);

        assertEquals(0, progress.progress());
        assertEquals(2, progress.duration());
        assertThrows(IllegalArgumentException.class, () -> progress.start(0));
    }

    @Test
    void resetEndsTheOperation() {
        progress.start(4);
        progress.advance();

        progress.reset();

        assertFalse(progress.isRunning());
        assertEquals(0, progress.progress());
        assertEquals(0, progress.duration());
    }

    @Test
    void restoreCorrectsInconsistentValues() {
        progress.restore(7, 5);
        assertEquals(5, progress.progress());
        assertEquals(5, progress.duration());

        progress.restore(-3, -1);
        assertEquals(0, progress.progress());
        assertEquals(0, progress.duration());
        assertFalse(progress.isRunning());

        progress.restore(2, 10);
        assertEquals(2, progress.progress());
        assertTrue(progress.isRunning());
    }
}
