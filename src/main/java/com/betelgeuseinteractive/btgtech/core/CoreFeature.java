package com.betelgeuseinteractive.btgtech.core;

import net.neoforged.bus.api.IEventBus;

/** Bootstrap of the shared infrastructure; the first feature that {@code BTGTech} registers. */
public final class CoreFeature {
    private CoreFeature() {}

    /** Registers the shared infrastructure on the mod event bus. */
    public static void register(IEventBus modBus) {
        CoreCreativeModeTabs.register(modBus);
    }
}
