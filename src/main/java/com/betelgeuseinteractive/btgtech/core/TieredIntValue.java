package com.betelgeuseinteractive.btgtech.core;

import java.util.EnumMap;
import java.util.Map;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * One configurable number per tier, for example the battery capacity. The defaults scale ×1 / ×4 / ×16 / ×64 from the
 * Basic value (ADR 0043).
 */
public final class TieredIntValue {
    private static final int TIER_FACTOR = 4;

    private final Map<Tier, ModConfigSpec.IntValue> values = new EnumMap<>(Tier.class);

    private TieredIntValue(ModConfigSpec.Builder builder, String name, String comment, int basicDefault) {
        builder.comment(comment).push(name);
        long defaultValue = basicDefault;
        for (Tier tier : Tier.values()) {
            values.put(tier, builder.defineInRange(tier.id(), (int) defaultValue, 1, Integer.MAX_VALUE));
            defaultValue *= TIER_FACTOR;
        }
        builder.pop();
    }

    /** Defines a section with one value per tier, named by the tier IDs. */
    static TieredIntValue define(ModConfigSpec.Builder builder, String name, String comment, int basicDefault) {
        return new TieredIntValue(builder, name, comment, basicDefault);
    }

    /**
     * Returns the value of the given tier. Read it when it is needed, never into a static field (ADR 0025).
     *
     * @throws IllegalStateException if the config is not loaded yet
     */
    public int get(Tier tier) {
        return values.get(tier).getAsInt();
    }
}
