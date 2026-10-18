package com.betelgeuseinteractive.btgtech.core.machine;

import com.betelgeuseinteractive.btgtech.core.machine.logic.RedstoneMode;
import com.betelgeuseinteractive.btgtech.core.machine.logic.SiPrefix;

/**
 * Translation keys of the machine screens (ADR 0051), shared by the screens and the language provider so that both
 * always use the same keys.
 */
public final class MachineTranslationKeys {
    /** The energy bar tooltip: stored and capacity. */
    public static final String ENERGY_BAR = "gui.btgtech.energy_bar";
    /** The hint below a tooltip that has details. */
    public static final String HOLD_SHIFT = "tooltip.btgtech.hold_shift";

    private static final String ENERGY = "gui.btgtech.energy";

    private MachineTranslationKeys() {}

    /** Returns the key of an energy amount with the given prefix, for example {@code 12.3 kFE}. */
    public static String energy(SiPrefix prefix) {
        return prefix == SiPrefix.NONE ? ENERGY : ENERGY + "." + prefix.id();
    }

    /** Returns the key of the name of the given redstone mode. */
    public static String redstoneMode(RedstoneMode mode) {
        return "gui.btgtech.redstone_mode." + mode.id();
    }
}
