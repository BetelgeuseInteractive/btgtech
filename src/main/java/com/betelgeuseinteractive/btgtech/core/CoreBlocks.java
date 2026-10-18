package com.betelgeuseinteractive.btgtech.core;

import com.betelgeuseinteractive.btgtech.BTGTech;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Blocks shared by several features: the machine casings, crafting components of the machine-like blocks of
 * {@code energy}, {@code machine} and {@code storage} (ADR 0040).
 */
public final class CoreBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BTGTech.MOD_ID);

    public static final DeferredBlock<Block> BASIC_MACHINE_CASING = BLOCKS.registerSimpleBlock(
            "basic_machine_casing",
            properties -> properties
                    .mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 6.0F)
                    .sound(SoundType.METAL));

    private CoreBlocks() {}

    /** Adds the blocks to the block registry. */
    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }
}
