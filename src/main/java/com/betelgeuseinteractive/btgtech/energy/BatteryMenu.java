package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.core.machine.MachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;

/** The battery menu: no machine slots, the player inventory and the energy and redstone mode data of the machine menu. */
public final class BatteryMenu extends MachineMenu {
    /** Creates the server-side menu. */
    BatteryMenu(int containerId, Inventory inventory, BatteryBlockEntity battery) {
        super(EnergyMenuTypes.BATTERY.get(), containerId, battery);
        addPlayerInventory(inventory);
    }

    /** Creates the client-side menu of the battery at the given position. */
    BatteryMenu(int containerId, Inventory inventory, BlockPos pos) {
        super(EnergyMenuTypes.BATTERY.get(), containerId, inventory, pos);
        addPlayerInventory(inventory);
    }
}
