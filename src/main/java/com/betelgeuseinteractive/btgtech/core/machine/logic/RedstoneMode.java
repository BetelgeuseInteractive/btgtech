package com.betelgeuseinteractive.btgtech.core.machine.logic;

/** When a machine may operate, depending on the redstone signal it receives (ADR 0035). */
public enum RedstoneMode {
    IGNORE("ignore"),
    ACTIVE_WITH_SIGNAL("active_with_signal"),
    ACTIVE_WITHOUT_SIGNAL("active_without_signal");

    private static final RedstoneMode[] VALUES = values();

    private final String id;

    RedstoneMode(String id) {
        this.id = id;
    }

    /** Returns the identifier used when saving; it never changes. */
    public String id() {
        return id;
    }

    /** Returns whether a machine with this mode may operate while it receives, or does not receive, a signal. */
    public boolean allowsOperation(boolean powered) {
        return switch (this) {
            case IGNORE -> true;
            case ACTIVE_WITH_SIGNAL -> powered;
            case ACTIVE_WITHOUT_SIGNAL -> !powered;
        };
    }

    /** Returns the mode that follows this one when the player cycles through them. */
    public RedstoneMode next() {
        return VALUES[(ordinal() + 1) % VALUES.length];
    }

    /** Returns the mode with the given identifier, or {@link #IGNORE} if none matches. */
    public static RedstoneMode byId(String id) {
        for (RedstoneMode mode : VALUES) {
            if (mode.id.equals(id)) {
                return mode;
            }
        }
        return IGNORE;
    }
}
