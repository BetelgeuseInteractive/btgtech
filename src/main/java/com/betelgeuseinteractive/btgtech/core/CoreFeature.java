package com.betelgeuseinteractive.btgtech.core;

import com.betelgeuseinteractive.btgtech.core.network.TransportNetworks;
import net.neoforged.bus.api.IEventBus;

/** Bootstrap of the shared infrastructure; the first feature that {@code BTGTech} registers. */
public final class CoreFeature {
    private CoreFeature() {}

    /** Registers the shared infrastructure on the mod and game event buses. */
    public static void register(IEventBus modBus) {
        CoreCreativeModeTabs.register(modBus);
        TransportNetworks.register();
    }
}
