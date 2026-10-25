package com.betelgeuseinteractive.btgtech.energy.client;

import com.betelgeuseinteractive.btgtech.energy.EnergyMenuTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/** Client bootstrap of the energy feature: its screens. */
public final class EnergyClientFeature {
    private EnergyClientFeature() {}

    /** Registers the client-only listeners of the energy feature. */
    public static void register(IEventBus modBus) {
        modBus.addListener(EnergyClientFeature::registerScreens);
    }

    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(EnergyMenuTypes.SOLID_FUEL_GENERATOR.get(), SolidFuelGeneratorScreen::new);
        event.register(EnergyMenuTypes.BATTERY.get(), BatteryScreen::new);
    }
}
