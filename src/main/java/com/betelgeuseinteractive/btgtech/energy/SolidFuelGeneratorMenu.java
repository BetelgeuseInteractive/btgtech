package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.core.machine.MachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

/** The solid fuel generator menu: the fuel slot, the player inventory and the state of the burning fuel. */
public final class SolidFuelGeneratorMenu extends MachineMenu {
    /** Screen position of the fuel slot. */
    public static final int FUEL_SLOT_X = 80;
    /** Screen position of the fuel slot. */
    public static final int FUEL_SLOT_Y = 46;

    private static final int FUEL_REMAINING = 0;
    private static final int FUEL_TOTAL = 1;
    private static final int DATA_SIZE = 2;

    private final ContainerData fuel;

    /** Creates the server-side menu. */
    SolidFuelGeneratorMenu(int containerId, Inventory inventory, SolidFuelGeneratorBlockEntity generator) {
        super(EnergyMenuTypes.SOLID_FUEL_GENERATOR.get(), containerId, generator);
        this.fuel = new FuelData(generator);
        addSlots(inventory);
    }

    /** Creates the client-side menu of the generator at the given position. */
    SolidFuelGeneratorMenu(int containerId, Inventory inventory, BlockPos pos) {
        super(EnergyMenuTypes.SOLID_FUEL_GENERATOR.get(), containerId, inventory, pos);
        this.fuel = new SimpleContainerData(DATA_SIZE);
        addSlots(inventory);
    }

    /** Returns the energy of the burning fuel that is not produced yet. */
    public int fuelRemaining() {
        return fuel.get(FUEL_REMAINING);
    }

    /** Returns the energy of the burning fuel when it started, or 0 when nothing burns. */
    public int fuelTotal() {
        return fuel.get(FUEL_TOTAL);
    }

    private void addSlots(Inventory inventory) {
        addMachineSlot(SolidFuelGeneratorBlockEntity.FUEL_SLOT, FUEL_SLOT_X, FUEL_SLOT_Y);
        addPlayerInventory(inventory);
        addDataSlots(fuel);
    }

    private static final class FuelData implements ContainerData {
        private final SolidFuelGeneratorBlockEntity generator;

        FuelData(SolidFuelGeneratorBlockEntity generator) {
            this.generator = generator;
        }

        @Override
        public int get(int index) {
            return index == FUEL_REMAINING ? generator.fuelRemaining() : generator.fuelTotal();
        }

        @Override
        public void set(int index, int value) {
            // The server-side values come from the block entity; only the client-side data is written.
        }

        @Override
        public int getCount() {
            return DATA_SIZE;
        }
    }
}
