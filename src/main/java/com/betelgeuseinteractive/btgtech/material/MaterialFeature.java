package com.betelgeuseinteractive.btgtech.material;

import com.betelgeuseinteractive.btgtech.core.CoreCreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/** Bootstrap of the materials feature: ores, metals and their forms (ADR 0038). */
public final class MaterialFeature {
    private MaterialFeature() {}

    /** Registers the material blocks and items and adds them to the creative tab. */
    public static void register(IEventBus modBus) {
        MaterialBlocks.register(modBus);
        MaterialItems.register(modBus);
        modBus.addListener(MaterialFeature::addToCreativeTab);
    }

    private static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CoreCreativeModeTabs.BTG_TECH.getKey())) {
            MaterialItems.CREATIVE_TAB_ORDER.forEach(event::accept);
        }
    }
}
