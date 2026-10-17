package com.betelgeuseinteractive.btgtech.core.machine.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class RedstoneModeTest {
    @Test
    void idsArePermanent() {
        List<String> ids =
                Arrays.stream(RedstoneMode.values()).map(RedstoneMode::id).toList();

        assertEquals(List.of("ignore", "active_with_signal", "active_without_signal"), ids);
    }

    @Test
    void allowsOperationFollowsTheSignal() {
        assertTrue(RedstoneMode.IGNORE.allowsOperation(true));
        assertTrue(RedstoneMode.IGNORE.allowsOperation(false));
        assertTrue(RedstoneMode.ACTIVE_WITH_SIGNAL.allowsOperation(true));
        assertFalse(RedstoneMode.ACTIVE_WITH_SIGNAL.allowsOperation(false));
        assertFalse(RedstoneMode.ACTIVE_WITHOUT_SIGNAL.allowsOperation(true));
        assertTrue(RedstoneMode.ACTIVE_WITHOUT_SIGNAL.allowsOperation(false));
    }

    @Test
    void nextCyclesThroughAllModes() {
        assertEquals(RedstoneMode.ACTIVE_WITH_SIGNAL, RedstoneMode.IGNORE.next());
        assertEquals(RedstoneMode.ACTIVE_WITHOUT_SIGNAL, RedstoneMode.ACTIVE_WITH_SIGNAL.next());
        assertEquals(RedstoneMode.IGNORE, RedstoneMode.ACTIVE_WITHOUT_SIGNAL.next());
    }

    @Test
    void byIdFindsModesAndFallsBackToIgnore() {
        for (RedstoneMode mode : RedstoneMode.values()) {
            assertEquals(mode, RedstoneMode.byId(mode.id()));
        }
        assertEquals(RedstoneMode.IGNORE, RedstoneMode.byId("unknown"));
    }
}
