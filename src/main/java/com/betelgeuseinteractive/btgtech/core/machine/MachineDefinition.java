package com.betelgeuseinteractive.btgtech.core.machine;

import com.betelgeuseinteractive.btgtech.core.Tier;
import java.util.function.ToIntFunction;
import org.jspecify.annotations.Nullable;

/**
 * The data that describes one machine-like block (ADR 0034): its slot layout and the components it uses. Tier values
 * are functions, read when a block entity is created, so that they can come from the {@code SERVER} config and are
 * never fixed at registration (ADR 0025).
 *
 * @param inputSlots the number of input slots
 * @param outputSlots the number of output slots, placed after the inputs
 * @param energy the energy limits, or {@code null} for blocks without energy
 */
public record MachineDefinition(
        int inputSlots, int outputSlots, @Nullable EnergyLimits energy) {
    public MachineDefinition {
        if (inputSlots < 0 || outputSlots < 0) {
            throw new IllegalArgumentException("Slot counts must not be negative: " + inputSlots + ", " + outputSlots);
        }
    }

    /** Returns the total number of item slots. */
    public int slotCount() {
        return inputSlots + outputSlots;
    }

    /**
     * Energy limits per tier, in FE.
     *
     * @param capacity the stored energy limit
     * @param maxInsert the most energy other blocks may insert per operation
     * @param maxExtract the most energy other blocks may extract per operation
     */
    public record EnergyLimits(
            ToIntFunction<Tier> capacity, ToIntFunction<Tier> maxInsert, ToIntFunction<Tier> maxExtract) {}
}
