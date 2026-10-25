package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.CoreItems;
import com.betelgeuseinteractive.btgtech.core.Tier;
import com.betelgeuseinteractive.btgtech.energy.EnergyItems;
import com.betelgeuseinteractive.btgtech.material.MaterialItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

/**
 * Crafting and smelting recipes, in per-feature sections. Ingredients are {@code c:} tags so that the same materials of
 * other mods work too (ADR 0038).
 */
final class RecipeDefinitionProvider extends RecipeProvider {
    private static final float ORE_EXPERIENCE = 0.7F;
    private static final int SMELTING_TICKS = 200;
    private static final int BLASTING_TICKS = 100;

    private RecipeDefinitionProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        addCore();
        addMaterials();
        addEnergy();
    }

    private void addCore() {
        shaped(RecipeCategory.MISC, CoreItems.BASIC_MACHINE_CASING.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', MaterialTags.BRONZE_INGOTS)
                .pattern("IBI")
                .pattern("B B")
                .pattern("IBI")
                .unlockedBy("has_bronze_ingot", has(MaterialTags.BRONZE_INGOTS))
                .save(output);
        shaped(RecipeCategory.MISC, CoreItems.BASIC_CIRCUIT.get())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('T', MaterialTags.TIN_INGOTS)
                .pattern(" R ")
                .pattern("CTC")
                .pattern(" R ")
                .unlockedBy("has_tin_ingot", has(MaterialTags.TIN_INGOTS))
                .save(output);
    }

    private void addMaterials() {
        metalForms(
                "tin",
                MaterialItems.TIN_INGOT.get(),
                MaterialItems.TIN_NUGGET.get(),
                MaterialItems.TIN_BLOCK.get(),
                MaterialTags.TIN_INGOTS,
                MaterialTags.TIN_NUGGETS,
                MaterialTags.TIN_STORAGE_BLOCKS);
        storage(
                MaterialItems.RAW_TIN_BLOCK.get(),
                MaterialTags.RAW_TIN,
                MaterialItems.RAW_TIN.get(),
                MaterialTags.RAW_TIN_STORAGE_BLOCKS,
                "raw_tin");
        cooking(MaterialItems.TIN_INGOT.get(), MaterialTags.TIN_ORES, "ore", ORE_EXPERIENCE);
        cooking(MaterialItems.TIN_INGOT.get(), MaterialTags.RAW_TIN, "raw_tin", ORE_EXPERIENCE);
        cooking(MaterialItems.TIN_INGOT.get(), MaterialTags.TIN_DUSTS, "dust", 0.0F);

        shapeless(RecipeCategory.MISC, MaterialItems.BRONZE_INGOT.get(), 4)
                .requires(tag(Tags.Items.INGOTS_COPPER), 3)
                .requires(MaterialTags.TIN_INGOTS)
                .unlockedBy("has_tin_ingot", has(MaterialTags.TIN_INGOTS))
                .save(output);
        metalForms(
                "bronze",
                MaterialItems.BRONZE_INGOT.get(),
                MaterialItems.BRONZE_NUGGET.get(),
                MaterialItems.BRONZE_BLOCK.get(),
                MaterialTags.BRONZE_INGOTS,
                MaterialTags.BRONZE_NUGGETS,
                MaterialTags.BRONZE_STORAGE_BLOCKS);
        cooking(MaterialItems.BRONZE_INGOT.get(), MaterialTags.BRONZE_DUSTS, "dust", 0.0F);
    }

    private void addEnergy() {
        // Basic only; the higher tiers follow with the tier upgrade recipes of WP-301.
        shaped(
                        RecipeCategory.REDSTONE,
                        EnergyItems.SOLID_FUEL_GENERATORS.get(Tier.BASIC).get())
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('F', Items.FURNACE)
                .define('M', CoreItems.BASIC_MACHINE_CASING.get())
                .define('K', CoreItems.BASIC_CIRCUIT.get())
                .pattern("CFC")
                .pattern("CMC")
                .pattern("CKC")
                .unlockedBy("has_basic_machine_casing", has(CoreItems.BASIC_MACHINE_CASING.get()))
                .save(output);
        shaped(RecipeCategory.REDSTONE, EnergyItems.BATTERIES.get(Tier.BASIC).get())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('M', CoreItems.BASIC_MACHINE_CASING.get())
                .define('K', CoreItems.BASIC_CIRCUIT.get())
                .pattern("RCR")
                .pattern("CMC")
                .pattern("RKR")
                .unlockedBy("has_basic_machine_casing", has(CoreItems.BASIC_MACHINE_CASING.get()))
                .save(output);
    }

    /** Ingot to nuggets and back, ingots to a storage block and back. */
    private void metalForms(
            String material,
            Item ingot,
            Item nugget,
            ItemLike block,
            TagKey<Item> ingots,
            TagKey<Item> nuggets,
            TagKey<Item> blocks) {
        shapeless(RecipeCategory.MISC, nugget, 9)
                .requires(ingots)
                .unlockedBy("has_" + material + "_ingot", has(ingots))
                .save(output);
        shaped(RecipeCategory.MISC, ingot)
                .define('#', nuggets)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group(material + "_ingot")
                .unlockedBy("has_" + material + "_nugget", has(nuggets))
                .save(output, id(material + "_ingot_from_nuggets"));
        storage(block, ingots, ingot, blocks, material + "_ingot");
    }

    /** Nine items to a storage block and the block back to nine items. */
    private void storage(ItemLike block, TagKey<Item> items, Item item, TagKey<Item> blocks, String itemName) {
        shaped(RecipeCategory.BUILDING_BLOCKS, block)
                .define('#', items)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_" + itemName, has(items))
                .save(output);
        shapeless(RecipeCategory.MISC, item, 9)
                .requires(blocks)
                .group(itemName)
                .unlockedBy("has_" + itemName + "_block", has(blocks))
                .save(output, id(itemName + "_from_block"));
    }

    /** Smelting and blasting of an ingredient into an ingot, blasting twice as fast. */
    private void cooking(Item ingot, TagKey<Item> ingredient, String source, float experience) {
        String ingotName = getItemName(ingot);
        SimpleCookingRecipeBuilder.smelting(
                        tag(ingredient),
                        RecipeCategory.MISC,
                        CookingBookCategory.MISC,
                        ingot,
                        experience,
                        SMELTING_TICKS)
                .group(ingotName)
                .unlockedBy("has_" + source, has(ingredient))
                .save(output, id(ingotName + "_from_smelting_" + source));
        SimpleCookingRecipeBuilder.blasting(
                        tag(ingredient),
                        RecipeCategory.MISC,
                        CookingBookCategory.MISC,
                        ingot,
                        experience,
                        BLASTING_TICKS)
                .group(ingotName)
                .unlockedBy("has_" + source, has(ingredient))
                .save(output, id(ingotName + "_from_blasting_" + source));
    }

    private static String id(String path) {
        return BTGTech.MOD_ID + ":" + path;
    }

    /** Runs the recipe provider in data generation. */
    static final class Runner extends RecipeProvider.Runner {
        Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new RecipeDefinitionProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes";
        }
    }
}
