package com.betelgeuseinteractive.btgtech.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * The {@code c:} tags of the material forms (ADR 0038, 0053). Parent tags defined by NeoForge come from {@code Tags};
 * {@code c:plates} is a convention among mods that NeoForge does not define.
 */
final class MaterialTags {
    static final TagKey<Block> TIN_ORES_BLOCK = block("ores/tin");
    static final TagKey<Block> TIN_STORAGE_BLOCKS_BLOCK = block("storage_blocks/tin");
    static final TagKey<Block> RAW_TIN_STORAGE_BLOCKS_BLOCK = block("storage_blocks/raw_tin");
    static final TagKey<Block> BRONZE_STORAGE_BLOCKS_BLOCK = block("storage_blocks/bronze");

    static final TagKey<Item> TIN_ORES = item("ores/tin");
    static final TagKey<Item> TIN_STORAGE_BLOCKS = item("storage_blocks/tin");
    static final TagKey<Item> RAW_TIN_STORAGE_BLOCKS = item("storage_blocks/raw_tin");
    static final TagKey<Item> BRONZE_STORAGE_BLOCKS = item("storage_blocks/bronze");
    static final TagKey<Item> RAW_TIN = item("raw_materials/tin");
    static final TagKey<Item> TIN_INGOTS = item("ingots/tin");
    static final TagKey<Item> TIN_NUGGETS = item("nuggets/tin");
    static final TagKey<Item> TIN_DUSTS = item("dusts/tin");
    static final TagKey<Item> TIN_PLATES = item("plates/tin");
    static final TagKey<Item> BRONZE_INGOTS = item("ingots/bronze");
    static final TagKey<Item> BRONZE_NUGGETS = item("nuggets/bronze");
    static final TagKey<Item> BRONZE_DUSTS = item("dusts/bronze");
    static final TagKey<Item> BRONZE_PLATES = item("plates/bronze");
    static final TagKey<Item> PLATES = item("plates");

    private MaterialTags() {}

    private static TagKey<Block> block(String path) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", path));
    }

    private static TagKey<Item> item(String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", path));
    }
}
