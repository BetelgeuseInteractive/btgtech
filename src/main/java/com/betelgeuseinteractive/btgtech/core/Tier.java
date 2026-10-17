package com.betelgeuseinteractive.btgtech.core;

/** The four machine tiers in progression order (ADR 0033). The set is closed (ADR 0069). */
public enum Tier {
    BASIC("basic", 0x4F555B, 0x7A828A, 0xA9B0B7),
    ADVANCED("advanced", 0x8C4A1E, 0xC9772F, 0xEDAA5F),
    ELITE("elite", 0x1D6272, 0x2EA2B7, 0x86E2EE),
    ULTIMATE("ultimate", 0x4F2672, 0x8B48BA, 0xC69EEA);

    private final String id;
    private final int darkColor;
    private final int midColor;
    private final int lightColor;

    Tier(String id, int darkColor, int midColor, int lightColor) {
        this.id = id;
        this.darkColor = darkColor;
        this.midColor = midColor;
        this.lightColor = lightColor;
    }

    /**
     * Returns the identifier used in registry IDs ({@code <tier>_<block>}) and resource paths. It never changes, because
     * registry IDs are permanent.
     */
    public String id() {
        return id;
    }

    /** Returns the dark tone of the tier color ramp as {@code 0xRRGGBB} (ADR 0074). */
    public int darkColor() {
        return darkColor;
    }

    /** Returns the mid tone of the tier color ramp as {@code 0xRRGGBB} (ADR 0074). */
    public int midColor() {
        return midColor;
    }

    /** Returns the light tone of the tier color ramp as {@code 0xRRGGBB} (ADR 0074). */
    public int lightColor() {
        return lightColor;
    }
}
