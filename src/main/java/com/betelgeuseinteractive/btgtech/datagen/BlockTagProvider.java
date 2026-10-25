package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.CoreBlocks;
import com.betelgeuseinteractive.btgtech.energy.EnergyBlocks;
import com.betelgeuseinteractive.btgtech.material.MaterialBlocks;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

/** Block tags: mining rules (ADR 0053) and the {@code c:} tags of the material blocks (ADR 0038). */
final class BlockTagProvider extends BlockTagsProvider {
    BlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BTGTech.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        addCore();
        addMaterials();
        addEnergy();
    }

    private void addCore() {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(CoreBlocks.BASIC_MACHINE_CASING.get());
        tag(BlockTags.NEEDS_STONE_TOOL).add(CoreBlocks.BASIC_MACHINE_CASING.get());
    }

    private void addMaterials() {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(MaterialBlocks.TIN_ORE.get())
                .add(MaterialBlocks.DEEPSLATE_TIN_ORE.get())
                .add(MaterialBlocks.TIN_BLOCK.get())
                .add(MaterialBlocks.RAW_TIN_BLOCK.get())
                .add(MaterialBlocks.BRONZE_BLOCK.get());
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(MaterialBlocks.TIN_ORE.get())
                .add(MaterialBlocks.DEEPSLATE_TIN_ORE.get())
                .add(MaterialBlocks.TIN_BLOCK.get())
                .add(MaterialBlocks.RAW_TIN_BLOCK.get())
                .add(MaterialBlocks.BRONZE_BLOCK.get());

        tag(MaterialTags.TIN_ORES_BLOCK).add(MaterialBlocks.TIN_ORE.get()).add(MaterialBlocks.DEEPSLATE_TIN_ORE.get());
        tag(Tags.Blocks.ORES).addTag(MaterialTags.TIN_ORES_BLOCK);
        tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(MaterialBlocks.TIN_ORE.get());
        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(MaterialBlocks.DEEPSLATE_TIN_ORE.get());
        tag(Tags.Blocks.ORE_RATES_SINGULAR)
                .add(MaterialBlocks.TIN_ORE.get())
                .add(MaterialBlocks.DEEPSLATE_TIN_ORE.get());

        tag(MaterialTags.TIN_STORAGE_BLOCKS_BLOCK).add(MaterialBlocks.TIN_BLOCK.get());
        tag(MaterialTags.RAW_TIN_STORAGE_BLOCKS_BLOCK).add(MaterialBlocks.RAW_TIN_BLOCK.get());
        tag(MaterialTags.BRONZE_STORAGE_BLOCKS_BLOCK).add(MaterialBlocks.BRONZE_BLOCK.get());
        tag(Tags.Blocks.STORAGE_BLOCKS)
                .addTag(MaterialTags.TIN_STORAGE_BLOCKS_BLOCK)
                .addTag(MaterialTags.RAW_TIN_STORAGE_BLOCKS_BLOCK)
                .addTag(MaterialTags.BRONZE_STORAGE_BLOCKS_BLOCK);
    }

    private void addEnergy() {
        Stream.concat(EnergyBlocks.SOLID_FUEL_GENERATORS.values().stream(), EnergyBlocks.BATTERIES.values().stream())
                .forEach(block -> {
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block.get());
                    tag(BlockTags.NEEDS_STONE_TOOL).add(block.get());
                });
    }
}
