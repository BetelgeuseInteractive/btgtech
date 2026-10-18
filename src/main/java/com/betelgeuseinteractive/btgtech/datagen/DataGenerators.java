package com.betelgeuseinteractive.btgtech.datagen;

import java.util.List;
import java.util.Set;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/** Registers BTG Tech's data providers; all of them run on {@link GatherDataEvent.Client} (ADR 0050). */
public final class DataGenerators {
    private DataGenerators() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(DataGenerators::gatherClientData);
    }

    private static void gatherClientData(GatherDataEvent.Client event) {
        // First, so that the providers below see the world generation entries in their registry lookup.
        event.createDatapackRegistryObjects(WorldGenerationEntries.builder());
        event.createProvider(EnglishLanguageProvider::new);
        event.createProvider(
                output -> new TierTextureProvider(output, event.getResourceManager(PackType.CLIENT_RESOURCES)));
        event.createProvider(ModelDefinitionProvider::new);
        event.createProvider(RecoloredSpriteProvider::new);
        event.createProvider((output, lookupProvider) -> new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(BlockLootProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider));
        event.createProvider(RecipeDefinitionProvider.Runner::new);
        event.createBlockAndItemTags(BlockTagProvider::new, ItemTagProvider::new);
    }
}
