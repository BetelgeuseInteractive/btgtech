package com.betelgeuseinteractive.btgtech.energy.client;

import com.betelgeuseinteractive.btgtech.core.machine.client.MachineScreen;
import com.betelgeuseinteractive.btgtech.energy.BatteryMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/** The battery screen: the standard machine screen with the energy bar and the redstone mode button only. */
public final class BatteryScreen extends MachineScreen<BatteryMenu> {
    public BatteryScreen(BatteryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }
}
