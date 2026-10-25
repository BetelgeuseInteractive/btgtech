package com.betelgeuseinteractive.btgtech.energy.client;

import com.betelgeuseinteractive.btgtech.core.machine.client.MachineScreen;
import com.betelgeuseinteractive.btgtech.energy.SolidFuelGeneratorMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/** The solid fuel generator screen: the machine screen with the fuel slot and the flame above it. */
public final class SolidFuelGeneratorScreen extends MachineScreen<SolidFuelGeneratorMenu> {
    private final FuelFlame flame = new FuelFlame(
            SolidFuelGeneratorMenu.FUEL_SLOT_X + 1, SolidFuelGeneratorMenu.FUEL_SLOT_Y - FuelFlame.SIZE - 4);

    public SolidFuelGeneratorScreen(SolidFuelGeneratorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        flame.extract(graphics, leftPos, topPos, menu.fuelRemaining(), menu.fuelTotal());
    }
}
