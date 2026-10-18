package com.betelgeuseinteractive.btgtech.datagen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.betelgeuseinteractive.btgtech.core.Tier;
import org.junit.jupiter.api.Test;

class TierRecolorTest {
    private static final int OPAQUE = 0xFF000000;

    @Test
    void eachBasicToneBecomesTheSameToneOfTheTier() {
        assertEquals(OPAQUE | 0x1D6272, TierRecolor.recolor(OPAQUE | Tier.BASIC.darkColor(), Tier.ELITE));
        assertEquals(OPAQUE | 0x2EA2B7, TierRecolor.recolor(OPAQUE | Tier.BASIC.midColor(), Tier.ELITE));
        assertEquals(OPAQUE | 0x86E2EE, TierRecolor.recolor(OPAQUE | Tier.BASIC.lightColor(), Tier.ELITE));
    }

    @Test
    void basicTonesStayForTheBasicTierAndAlphaIsKept() {
        int translucentMid = 0x80000000 | Tier.BASIC.midColor();

        assertEquals(translucentMid, TierRecolor.recolor(translucentMid, Tier.BASIC));
    }

    @Test
    void otherColorsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> TierRecolor.recolor(0xFFC6C6C6, Tier.ADVANCED));
    }
}
