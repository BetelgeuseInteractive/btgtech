package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import net.minecraft.resources.Identifier;

/**
 * Sprites of the material forms, recolored at runtime (ADR 0074, art style guide section 5). The model and sprite source
 * providers both use these names, so a model always points to a sprite the atlas creates.
 */
final class MaterialTextures {
    /** Separator of recolored vanilla sprites; it keeps them apart from sprites other mods derive from the same texture. */
    static final String VANILLA_SEPARATOR = "_btgtech_";
    /** Separator of recolored BTG Tech templates. */
    static final String TEMPLATE_SEPARATOR = "_";

    static final String TIN = "tin";
    static final String BRONZE = "bronze";

    private MaterialTextures() {}

    /** Returns the sprite of a vanilla texture recolored for a material, for example {@code block/iron_ore} for tin. */
    static Identifier recoloredVanilla(String vanillaTexture, String material) {
        return Identifier.withDefaultNamespace(vanillaTexture + VANILLA_SEPARATOR + material);
    }

    /** Returns the sprite of a BTG Tech template recolored for a material, for example {@code item/dust} for tin. */
    static Identifier recoloredTemplate(String template, String material) {
        return Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, template + TEMPLATE_SEPARATOR + material);
    }

    /** Returns a palette strip, for example {@code iron_ore_key} or {@code iron_ore_tin}. */
    static Identifier palette(String name) {
        return Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, "palettes/" + name);
    }
}
