package com.betelgeuseinteractive.btgtech.core;

/** The four machine tiers in progression order (ADR 0033). The set is closed (ADR 0069). */
public enum Tier {
    BASIC("basic"),
    ADVANCED("advanced"),
    ELITE("elite"),
    ULTIMATE("ultimate");

    private final String id;

    Tier(String id) {
        this.id = id;
    }

    /**
     * Returns the identifier used in registry IDs ({@code <tier>_<block>}) and resource paths. It never changes, because
     * registry IDs are permanent.
     */
    public String id() {
        return id;
    }
}
