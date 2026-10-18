package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.CoreBlocks;
import com.betelgeuseinteractive.btgtech.core.CoreItems;
import com.betelgeuseinteractive.btgtech.material.MaterialBlocks;
import com.betelgeuseinteractive.btgtech.material.MaterialItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * Block states, block and item models and client item definitions, in per-feature sections (ADR 0050). Block items use
 * their block's model.
 */
final class ModelDefinitionProvider extends ModelProvider {
    ModelDefinitionProvider(PackOutput output) {
        super(output, BTGTech.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        addCore(blockModels, itemModels);
        addMaterials(blockModels, itemModels);
    }

    private static void addCore(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // Crafting components: textures generated per tier (TierTextureProvider)
        blockModels.createTrivialCube(CoreBlocks.BASIC_MACHINE_CASING.get());
        itemModels.generateFlatItem(CoreItems.BASIC_CIRCUIT.get(), ModelTemplates.FLAT_ITEM);
    }

    private static void addMaterials(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        recoloredCube(blockModels, MaterialBlocks.TIN_ORE, "block/iron_ore", MaterialTextures.TIN);
        recoloredCube(blockModels, MaterialBlocks.DEEPSLATE_TIN_ORE, "block/deepslate_iron_ore", MaterialTextures.TIN);
        recoloredItem(itemModels, MaterialItems.RAW_TIN, "item/raw_iron", MaterialTextures.TIN);
        recoloredItem(itemModels, MaterialItems.TIN_INGOT, "item/iron_ingot", MaterialTextures.TIN);
        recoloredItem(itemModels, MaterialItems.TIN_NUGGET, "item/iron_nugget", MaterialTextures.TIN);
        templateItem(itemModels, MaterialItems.TIN_DUST, "item/dust", MaterialTextures.TIN);
        templateItem(itemModels, MaterialItems.TIN_PLATE, "item/plate", MaterialTextures.TIN);
        recoloredCube(blockModels, MaterialBlocks.TIN_BLOCK, "block/iron_block", MaterialTextures.TIN);
        recoloredCube(blockModels, MaterialBlocks.RAW_TIN_BLOCK, "block/raw_iron_block", MaterialTextures.TIN);

        recoloredItem(itemModels, MaterialItems.BRONZE_INGOT, "item/iron_ingot", MaterialTextures.BRONZE);
        recoloredItem(itemModels, MaterialItems.BRONZE_NUGGET, "item/iron_nugget", MaterialTextures.BRONZE);
        templateItem(itemModels, MaterialItems.BRONZE_DUST, "item/dust", MaterialTextures.BRONZE);
        templateItem(itemModels, MaterialItems.BRONZE_PLATE, "item/plate", MaterialTextures.BRONZE);
        recoloredCube(blockModels, MaterialBlocks.BRONZE_BLOCK, "block/iron_block", MaterialTextures.BRONZE);
    }

    private static void recoloredCube(
            BlockModelGenerators blockModels, DeferredBlock<Block> block, String vanillaTexture, String material) {
        Material sprite = new Material(MaterialTextures.recoloredVanilla(vanillaTexture, material));
        blockModels.createTrivialBlock(
                block.get(), TexturedModel.CUBE.updateTexture(textures -> textures.put(TextureSlot.ALL, sprite)));
    }

    private static void recoloredItem(
            ItemModelGenerators itemModels, DeferredItem<Item> item, String vanillaTexture, String material) {
        flatItem(itemModels, item.get(), MaterialTextures.recoloredVanilla(vanillaTexture, material));
    }

    private static void templateItem(
            ItemModelGenerators itemModels, DeferredItem<Item> item, String template, String material) {
        flatItem(itemModels, item.get(), MaterialTextures.recoloredTemplate(template, material));
    }

    private static void flatItem(ItemModelGenerators itemModels, Item item, Identifier sprite) {
        Identifier model = ModelTemplates.FLAT_ITEM.create(
                ModelLocationUtils.getModelLocation(item),
                TextureMapping.layer0(new Material(sprite)),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
    }
}
