package com.betelgeuseinteractive.btgtech.core;

import com.betelgeuseinteractive.btgtech.core.network.TransportNetworks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/** Bootstrap of the shared infrastructure; the first feature that {@code BTGTech} registers. */
public final class CoreFeature {
    private CoreFeature() {}

    /** Registers the shared infrastructure on the mod and game event buses. */
    public static void register(IEventBus modBus) {
        CoreCreativeModeTabs.register(modBus);
        CoreBlocks.register(modBus);
        CoreItems.register(modBus);
        modBus.addListener(CoreFeature::addToCreativeTab);
        TransportNetworks.register();
    }

    private static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CoreCreativeModeTabs.BTG_TECH.getKey())) {
            CoreItems.CREATIVE_TAB_ORDER.forEach(event::accept);
        }
    }
}
