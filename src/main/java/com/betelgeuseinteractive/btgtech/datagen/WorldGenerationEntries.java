package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.material.MaterialBlocks;
import java.util.List;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * World generation entries (ADR 0053): configured and placed ore features and the biome modifier that adds them to the
 * overworld. Parameters from the balance table (balance.md, section 5).
 */
final class WorldGenerationEntries {
    static final ResourceKey<ConfiguredFeature<?, ?>> TIN_ORE_CONFIGURED =
            key(Registries.CONFIGURED_FEATURE, "ore_tin");
    static final ResourceKey<PlacedFeature> TIN_ORE_PLACED = key(Registries.PLACED_FEATURE, "ore_tin");
    static final ResourceKey<BiomeModifier> TIN_ORE_MODIFIER =
            key(NeoForgeRegistries.Keys.BIOME_MODIFIERS, "add_ore_tin");

    private static final int TIN_VEIN_SIZE = 9;
    private static final int TIN_VEINS_PER_CHUNK = 10;
    private static final int TIN_MIN_Y = -16;
    private static final int TIN_MAX_Y = 96;

    private WorldGenerationEntries() {}

    /** Returns the entries for {@code GatherDataEvent.createDatapackRegistryObjects}. */
    static RegistrySetBuilder builder() {
        return new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, WorldGenerationEntries::configuredFeatures)
                .add(Registries.PLACED_FEATURE, WorldGenerationEntries::placedFeatures)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, WorldGenerationEntries::biomeModifiers);
    }

    private static void configuredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        List<OreConfiguration.TargetBlockState> tinTargets = List.of(
                OreConfiguration.target(
                        new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES),
                        MaterialBlocks.TIN_ORE.get().defaultBlockState()),
                OreConfiguration.target(
                        new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                        MaterialBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState()));
        context.register(
                TIN_ORE_CONFIGURED,
                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(tinTargets, TIN_VEIN_SIZE)));
    }

    private static void placedFeatures(BootstrapContext<PlacedFeature> context) {
        context.register(
                TIN_ORE_PLACED,
                new PlacedFeature(
                        context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(TIN_ORE_CONFIGURED),
                        List.of(
                                CountPlacement.of(TIN_VEINS_PER_CHUNK),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.triangle(
                                        VerticalAnchor.absolute(TIN_MIN_Y), VerticalAnchor.absolute(TIN_MAX_Y)),
                                BiomeFilter.biome())));
    }

    private static void biomeModifiers(BootstrapContext<BiomeModifier> context) {
        context.register(
                TIN_ORE_MODIFIER,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD),
                        HolderSet.direct(
                                context.lookup(Registries.PLACED_FEATURE).getOrThrow(TIN_ORE_PLACED)),
                        GenerationStep.Decoration.UNDERGROUND_ORES));
    }

    private static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String path) {
        return ResourceKey.create(registry, Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, path));
    }
}
