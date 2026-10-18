package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;

/**
 * Atlas sources that recolor textures at runtime with {@code minecraft:paletted_permutations} (ADR 0074): the vanilla iron
 * textures of the forms with a vanilla counterpart, and the BTG Tech dust and plate templates. Each source has a palette
 * key with the colors to replace and one palette per material.
 */
final class RecoloredSpriteProvider extends SpriteSourceProvider {
    RecoloredSpriteProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BTGTech.MOD_ID);
    }

    @Override
    protected void gather() {
        SourceList blocks = atlas(AtlasIds.BLOCKS);
        recolorVanilla(blocks, "block/iron_ore", MaterialTextures.TIN);
        recolorVanilla(blocks, "block/deepslate_iron_ore", MaterialTextures.TIN);
        recolorVanilla(blocks, "block/raw_iron_block", MaterialTextures.TIN);
        recolorVanilla(blocks, "block/iron_block", MaterialTextures.TIN, MaterialTextures.BRONZE);

        SourceList items = atlas(AtlasIds.ITEMS);
        recolorVanilla(items, "item/raw_iron", MaterialTextures.TIN);
        recolorVanilla(items, "item/iron_ingot", MaterialTextures.TIN, MaterialTextures.BRONZE);
        recolorVanilla(items, "item/iron_nugget", MaterialTextures.TIN, MaterialTextures.BRONZE);
        recolorTemplate(items, "item/dust", MaterialTextures.TIN, MaterialTextures.BRONZE);
        recolorTemplate(items, "item/plate", MaterialTextures.TIN, MaterialTextures.BRONZE);
    }

    private static void recolorVanilla(SourceList atlas, String texture, String... materials) {
        atlas.addSource(permutations(
                Identifier.withDefaultNamespace(texture),
                paletteName(texture),
                MaterialTextures.VANILLA_SEPARATOR,
                materials));
    }

    private static void recolorTemplate(SourceList atlas, String texture, String... materials) {
        atlas.addSource(permutations(
                Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, texture),
                paletteName(texture),
                MaterialTextures.TEMPLATE_SEPARATOR,
                materials));
    }

    private static PalettedPermutations permutations(
            Identifier texture, String paletteName, String separator, String... materials) {
        Map<String, Identifier> palettes = new LinkedHashMap<>();
        for (String material : materials) {
            palettes.put(material, MaterialTextures.palette(paletteName + "_" + material));
        }
        return new PalettedPermutations(
                List.of(texture), MaterialTextures.palette(paletteName + "_key"), palettes, separator);
    }

    // Palette strips are named after the texture file: block/iron_ore -> iron_ore_key, iron_ore_tin.
    private static String paletteName(String texture) {
        return texture.substring(texture.lastIndexOf('/') + 1);
    }
}
