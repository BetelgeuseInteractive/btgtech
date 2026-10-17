package com.betelgeuseinteractive.btgtech.core.machine;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/**
 * Registers the capabilities of machine block entities (ADR 0026, 0035). Features call this from their
 * {@link RegisterCapabilitiesEvent} listener for each machine block entity type.
 */
public final class MachineCapabilities {
    private MachineCapabilities() {}

    /**
     * Registers the energy and item handlers of the block entity type; closed faces and missing components expose
     * nothing.
     */
    public static <M extends MachineBlockEntity> void register(
            RegisterCapabilitiesEvent event, BlockEntityType<M> type) {
        event.registerBlockEntity(Capabilities.Energy.BLOCK, type, MachineBlockEntity::energyHandler);
        event.registerBlockEntity(Capabilities.Item.BLOCK, type, MachineBlockEntity::itemHandler);
    }
}
