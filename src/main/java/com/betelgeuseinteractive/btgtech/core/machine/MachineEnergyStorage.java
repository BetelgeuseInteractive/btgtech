package com.betelgeuseinteractive.btgtech.core.machine;

import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;

/**
 * The energy storage of every BTG energy block (ADR 0026, 0032). Other blocks transfer energy within the tier limits;
 * the owning block produces and uses energy directly. Every change is reported to the owner.
 */
public final class MachineEnergyStorage extends SimpleEnergyHandler {
    private final Runnable onChange;

    /**
     * Creates an empty storage.
     *
     * @param onChange called after every change, for example to mark the owning block entity as changed
     */
    public MachineEnergyStorage(int capacity, int maxInsert, int maxExtract, Runnable onChange) {
        super(capacity, maxInsert, maxExtract);
        this.onChange = onChange;
    }

    /** Returns the stored energy. */
    public int stored() {
        return energy;
    }

    /** Returns the energy limit. */
    public int capacity() {
        return capacity;
    }

    /** Adds energy produced by the owning block, ignoring the insertion limit, and returns the amount added. */
    public int generate(int amount) {
        int added = Math.clamp(amount, 0, capacity - energy);
        if (added > 0) {
            set(energy + added);
        }
        return added;
    }

    /** Removes energy used by the owning block, ignoring the extraction limit, and returns the amount removed. */
    public int consume(int amount) {
        int removed = Math.clamp(amount, 0, energy);
        if (removed > 0) {
            set(energy - removed);
        }
        return removed;
    }

    @Override
    public void deserialize(ValueInput input) {
        super.deserialize(input);
        // The capacity may have been lowered in the config since the energy was saved.
        energy = Math.min(energy, capacity);
    }

    @Override
    protected void onEnergyChanged(int previousAmount) {
        onChange.run();
    }
}
