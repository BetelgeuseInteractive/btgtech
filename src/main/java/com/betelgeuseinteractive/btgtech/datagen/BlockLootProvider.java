package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.CoreBlocks;
import com.betelgeuseinteractive.btgtech.material.MaterialBlocks;
import com.betelgeuseinteractive.btgtech.material.MaterialItems;
import java.util.Set;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

/** Block loot tables (ADR 0052), in per-feature sections: ores drop raw ore as in vanilla, other blocks themselves. */
final class BlockLootProvider extends BlockLootSubProvider {
    BlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        addCore();
        addMaterials();
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK
                .listElements()
                .filter(holder -> holder.key().identifier().getNamespace().equals(BTGTech.MOD_ID))
                .map(holder -> (Block) holder.value())
                .toList();
    }

    private void addCore() {
        dropSelf(CoreBlocks.BASIC_MACHINE_CASING.get());
    }

    private void addMaterials() {
        add(MaterialBlocks.TIN_ORE.get(), block -> createOreDrop(block, MaterialItems.RAW_TIN.get()));
        add(MaterialBlocks.DEEPSLATE_TIN_ORE.get(), block -> createOreDrop(block, MaterialItems.RAW_TIN.get()));
        dropSelf(MaterialBlocks.TIN_BLOCK.get());
        dropSelf(MaterialBlocks.RAW_TIN_BLOCK.get());
        dropSelf(MaterialBlocks.BRONZE_BLOCK.get());
    }
}
