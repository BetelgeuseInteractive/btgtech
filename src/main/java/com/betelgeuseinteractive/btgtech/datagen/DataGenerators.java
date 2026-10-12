package com.betelgeuseinteractive.btgtech.datagen;

import net.minecraft.server.packs.PackType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/** Registers BTG Tech's data providers; all of them run on {@link GatherDataEvent.Client} (ADR 0050). */
public final class DataGenerators {
    private DataGenerators() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(DataGenerators::gatherClientData);
    }

    private static void gatherClientData(GatherDataEvent.Client event) {
        event.createProvider(EnglishLanguageProvider::new);
        event.createProvider(
                output -> new PngProofProvider(output, event.getResourceManager(PackType.CLIENT_RESOURCES)));
    }
}
