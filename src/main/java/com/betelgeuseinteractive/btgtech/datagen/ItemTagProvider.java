package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.material.MaterialItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider;

/** Item tags: the {@code c:} tags of the material forms (ADR 0038); block item tags are copied from the block tags. */
final class ItemTagProvider extends BlockTagCopyingItemTagProvider {
    ItemTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, BTGTech.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        addMaterials();
    }

    private void addMaterials() {
        copy(MaterialTags.TIN_ORES_BLOCK, MaterialTags.TIN_ORES);
        copy(Tags.Blocks.ORES, Tags.Items.ORES);
        copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE);
        copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE);
        copy(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR);
        copy(MaterialTags.TIN_STORAGE_BLOCKS_BLOCK, MaterialTags.TIN_STORAGE_BLOCKS);
        copy(MaterialTags.RAW_TIN_STORAGE_BLOCKS_BLOCK, MaterialTags.RAW_TIN_STORAGE_BLOCKS);
        copy(MaterialTags.BRONZE_STORAGE_BLOCKS_BLOCK, MaterialTags.BRONZE_STORAGE_BLOCKS);
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);

        tag(MaterialTags.RAW_TIN).add(MaterialItems.RAW_TIN.get());
        tag(Tags.Items.RAW_MATERIALS).addTag(MaterialTags.RAW_TIN);
        tag(MaterialTags.TIN_INGOTS).add(MaterialItems.TIN_INGOT.get());
        tag(MaterialTags.BRONZE_INGOTS).add(MaterialItems.BRONZE_INGOT.get());
        tag(Tags.Items.INGOTS).addTag(MaterialTags.TIN_INGOTS).addTag(MaterialTags.BRONZE_INGOTS);
        tag(MaterialTags.TIN_NUGGETS).add(MaterialItems.TIN_NUGGET.get());
        tag(MaterialTags.BRONZE_NUGGETS).add(MaterialItems.BRONZE_NUGGET.get());
        tag(Tags.Items.NUGGETS).addTag(MaterialTags.TIN_NUGGETS).addTag(MaterialTags.BRONZE_NUGGETS);
        tag(MaterialTags.TIN_DUSTS).add(MaterialItems.TIN_DUST.get());
        tag(MaterialTags.BRONZE_DUSTS).add(MaterialItems.BRONZE_DUST.get());
        tag(Tags.Items.DUSTS).addTag(MaterialTags.TIN_DUSTS).addTag(MaterialTags.BRONZE_DUSTS);
        tag(MaterialTags.TIN_PLATES).add(MaterialItems.TIN_PLATE.get());
        tag(MaterialTags.BRONZE_PLATES).add(MaterialItems.BRONZE_PLATE.get());
        tag(MaterialTags.PLATES).addTag(MaterialTags.TIN_PLATES).addTag(MaterialTags.BRONZE_PLATES);
    }
}
