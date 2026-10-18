package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.Tier;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Blocks of the energy feature, one per tier (ADR 0033): {@code <tier>_battery}. */
public final class EnergyBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BTGTech.MOD_ID);

    /** The batteries by tier. */
    public static final Map<Tier, DeferredBlock<BatteryBlock>> BATTERIES = registerBatteries();

    private EnergyBlocks() {}

    /** Adds the blocks to the block registry. */
    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }

    private static Map<Tier, DeferredBlock<BatteryBlock>> registerBatteries() {
        Map<Tier, DeferredBlock<BatteryBlock>> batteries = new EnumMap<>(Tier.class);
        for (Tier tier : Tier.values()) {
            batteries.put(
                    tier,
                    BLOCKS.registerBlock(
                            tier.id() + "_battery",
                            properties -> new BatteryBlock(properties, tier),
                            properties -> properties
                                    .mapColor(MapColor.METAL)
                                    .requiresCorrectToolForDrops()
                                    .strength(3.5F, 6.0F)
                                    .sound(SoundType.METAL)));
        }
        return Collections.unmodifiableMap(batteries);
    }
}
