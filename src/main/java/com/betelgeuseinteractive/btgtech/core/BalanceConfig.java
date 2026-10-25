package com.betelgeuseinteractive.btgtech.core;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * The {@code SERVER} config with all balance values (ADR 0025): synchronized to clients and overridable per world.
 * Defaults come from the balance table ({@code docs/architecture/balance.md}); each system adds its section with the
 * work package that introduces it.
 */
public final class BalanceConfig {
    /** Energy a battery stores, in FE (balance.md, section 3). */
    public static final TieredIntValue BATTERY_CAPACITY;
    /** Energy a battery accepts or gives per tick, in FE; the same as a cable of its tier (balance.md, section 3). */
    public static final TieredIntValue BATTERY_TRANSFER;
    /** Energy a solid fuel generator produces per tick while burning, in FE (balance.md, section 2). */
    public static final TieredIntValue SOLID_FUEL_GENERATOR_OUTPUT;
    /** Energy a solid fuel generator holds before it stops burning, in FE (balance.md, section 2). */
    public static final TieredIntValue SOLID_FUEL_GENERATOR_BUFFER;
    /** Energy other blocks may take from a solid fuel generator per tick, in FE (balance.md, section 2). */
    public static final TieredIntValue SOLID_FUEL_GENERATOR_EXTRACT;
    /** Energy per tick of burn time, in FE, the same on every tier (ADR 0043; balance.md, section 2). */
    public static final ModConfigSpec.IntValue SOLID_FUEL_ENERGY_PER_BURN_TICK;

    static final ModConfigSpec SPEC;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("Batteries").push("battery");
        BATTERY_CAPACITY = TieredIntValue.define(builder, "capacity", "Stored energy in FE", 1_000_000);
        BATTERY_TRANSFER = TieredIntValue.define(builder, "transfer", "Energy accepted or given per tick in FE", 1_000);
        builder.pop();
        builder.comment("Solid fuel generators").push("solid_fuel_generator");
        SOLID_FUEL_GENERATOR_OUTPUT =
                TieredIntValue.define(builder, "output", "Energy produced per tick while burning in FE", 40);
        SOLID_FUEL_GENERATOR_BUFFER =
                TieredIntValue.define(builder, "buffer", "Stored energy before burning stops in FE", 10_000);
        SOLID_FUEL_GENERATOR_EXTRACT =
                TieredIntValue.define(builder, "extract", "Energy other blocks may take per tick in FE", 1_000);
        SOLID_FUEL_ENERGY_PER_BURN_TICK = builder.comment("Energy per tick of a fuel's burn time in FE, on every tier")
                .defineInRange("energy_per_burn_tick", 40, 1, Integer.MAX_VALUE);
        builder.pop();
        SPEC = builder.build();
    }

    private BalanceConfig() {}
}
