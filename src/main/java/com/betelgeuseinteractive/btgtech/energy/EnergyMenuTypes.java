package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.BTGTech;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Menu types of the energy feature; the client reads the block position the server sends when opening. */
public final class EnergyMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, BTGTech.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<SolidFuelGeneratorMenu>> SOLID_FUEL_GENERATOR =
            MENU_TYPES.register(
                    "solid_fuel_generator",
                    () -> IMenuTypeExtension.create((containerId, inventory, buffer) ->
                            new SolidFuelGeneratorMenu(containerId, inventory, buffer.readBlockPos())));
    public static final DeferredHolder<MenuType<?>, MenuType<BatteryMenu>> BATTERY = MENU_TYPES.register(
            "battery",
            () -> IMenuTypeExtension.create((containerId, inventory, buffer) ->
                    new BatteryMenu(containerId, inventory, buffer.readBlockPos())));

    private EnergyMenuTypes() {}

    /** Adds the menu types to their registry. */
    public static void register(IEventBus modBus) {
        MENU_TYPES.register(modBus);
    }
}
