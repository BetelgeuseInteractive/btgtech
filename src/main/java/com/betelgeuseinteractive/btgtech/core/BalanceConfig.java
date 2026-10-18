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

    static final ModConfigSpec SPEC;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("Batteries").push("battery");
        BATTERY_CAPACITY = TieredIntValue.define(builder, "capacity", "Stored energy in FE", 1_000_000);
        BATTERY_TRANSFER = TieredIntValue.define(builder, "transfer", "Energy accepted or given per tick in FE", 1_000);
        builder.pop();
        SPEC = builder.build();
    }

    private BalanceConfig() {}
}
