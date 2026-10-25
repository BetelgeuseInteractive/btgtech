package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.Tier;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Blocks of the energy feature, one per tier (ADR 0033): {@code <tier>_solid_fuel_generator}, {@code <tier>_battery}.
 */
public final class EnergyBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BTGTech.MOD_ID);

    /** The solid fuel generators by tier. */
    public static final Map<Tier, DeferredBlock<SolidFuelGeneratorBlock>> SOLID_FUEL_GENERATORS =
            registerTiered("solid_fuel_generator", SolidFuelGeneratorBlock::new);
    /** The batteries by tier. */
    public static final Map<Tier, DeferredBlock<BatteryBlock>> BATTERIES = registerTiered("battery", BatteryBlock::new);

    private EnergyBlocks() {}

    /** Adds the blocks to the block registry. */
    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }

    // Machine-like blocks of one kind, one per tier, with the properties of machine blocks.
    private static <B extends Block> Map<Tier, DeferredBlock<B>> registerTiered(
            String name, BiFunction<BlockBehaviour.Properties, Tier, B> factory) {
        Map<Tier, DeferredBlock<B>> blocks = new EnumMap<>(Tier.class);
        for (Tier tier : Tier.values()) {
            blocks.put(
                    tier,
                    BLOCKS.registerBlock(
                            tier.id() + "_" + name,
                            properties -> factory.apply(properties, tier),
                            properties -> properties
                                    .mapColor(MapColor.METAL)
                                    .requiresCorrectToolForDrops()
                                    .strength(3.5F, 6.0F)
                                    .sound(SoundType.METAL)));
        }
        return Collections.unmodifiableMap(blocks);
    }
}
