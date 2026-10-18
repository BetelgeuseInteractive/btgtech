package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.core.CoreCreativeModeTabs;
import com.betelgeuseinteractive.btgtech.core.machine.MachineCapabilities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/** Bootstrap of the energy feature: batteries, later generators and cables (ADR 0018). */
public final class EnergyFeature {
    private EnergyFeature() {}

    /** Registers the energy blocks, items, block entities, menus and game tests, and the feature's listeners. */
    public static void register(IEventBus modBus) {
        EnergyBlocks.register(modBus);
        EnergyItems.register(modBus);
        EnergyBlockEntityTypes.register(modBus);
        EnergyMenuTypes.register(modBus);
        EnergyGameTests.register(modBus);
        modBus.addListener(EnergyFeature::registerCapabilities);
        modBus.addListener(EnergyFeature::addToCreativeTab);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        MachineCapabilities.register(event, EnergyBlockEntityTypes.BATTERY.get());
    }

    private static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CoreCreativeModeTabs.BTG_TECH.getKey())) {
            EnergyItems.creativeTabOrder().forEach(event::accept);
        }
    }
}
