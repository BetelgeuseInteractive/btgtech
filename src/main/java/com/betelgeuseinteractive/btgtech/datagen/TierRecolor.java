package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.core.Tier;
import java.util.Locale;

/**
 * Replaces a Basic tier tone with the matching tone of another tier, for the pixels under a tier mask (art style
 * guide, section 4).
 */
final class TierRecolor {
    private TierRecolor() {}

    /**
     * Returns the pixel with its Basic tier tone replaced by the same tone of the given tier; alpha is kept.
     *
     * @param argb a pixel as {@code 0xAARRGGBB}
     * @throws IllegalArgumentException if the pixel is not one of the three Basic tier tones
     */
    static int recolor(int argb, Tier tier) {
        int rgb = argb & 0xFFFFFF;
        int alpha = argb & 0xFF000000;
        if (rgb == Tier.BASIC.darkColor()) {
            return alpha | tier.darkColor();
        }
        if (rgb == Tier.BASIC.midColor()) {
            return alpha | tier.midColor();
        }
        if (rgb == Tier.BASIC.lightColor()) {
            return alpha | tier.lightColor();
        }
        throw new IllegalArgumentException(String.format(Locale.ROOT, "#%06X is not a Basic tier tone", rgb));
    }
}
