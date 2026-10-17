package com.betelgeuseinteractive.btgtech.core.machine.logic;

/** The SI prefixes amounts are shown with (gui.md, section 5). */
public enum SiPrefix {
    NONE("", 1L),
    KILO("kilo", 1_000L),
    MEGA("mega", 1_000_000L),
    GIGA("giga", 1_000_000_000L);

    private final String id;
    private final long factor;

    SiPrefix(String id, long factor) {
        this.id = id;
        this.factor = factor;
    }

    /** Returns the identifier used in translation keys, empty for {@link #NONE}. */
    public String id() {
        return id;
    }

    /** Returns the value of one unit with this prefix. */
    public long factor() {
        return factor;
    }
}
