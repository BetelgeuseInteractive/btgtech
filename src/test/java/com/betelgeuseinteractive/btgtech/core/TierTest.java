package com.betelgeuseinteractive.btgtech.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class TierTest {
    @Test
    void tiersKeepTheirProgressionOrderAndIds() {
        List<String> ids = Arrays.stream(Tier.values()).map(Tier::id).toList();

        assertEquals(List.of("basic", "advanced", "elite", "ultimate"), ids);
    }
}
