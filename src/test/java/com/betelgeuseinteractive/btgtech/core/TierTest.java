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

    @Test
    void tierColorsMatchTheArtStyle() {
        List<List<Integer>> ramps = Arrays.stream(Tier.values())
                .map(tier -> List.of(tier.darkColor(), tier.midColor(), tier.lightColor()))
                .toList();

        assertEquals(
                List.of(
                        List.of(0x4F555B, 0x7A828A, 0xA9B0B7),
                        List.of(0x8C4A1E, 0xC9772F, 0xEDAA5F),
                        List.of(0x1D6272, 0x2EA2B7, 0x86E2EE),
                        List.of(0x4F2672, 0x8B48BA, 0xC69EEA)),
                ramps,
                "ADR 0074, decision 3");
    }
}
