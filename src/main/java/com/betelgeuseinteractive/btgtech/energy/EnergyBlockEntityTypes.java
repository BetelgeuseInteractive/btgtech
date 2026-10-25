package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.BTGTech;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Block entity types of the energy feature; one type serves all four tier blocks. */
public final class EnergyBlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, BTGTech.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SolidFuelGeneratorBlockEntity>>
            SOLID_FUEL_GENERATOR = BLOCK_ENTITY_TYPES.register(
                    "solid_fuel_generator",
                    () -> new BlockEntityType<>(
                            SolidFuelGeneratorBlockEntity::new, blocks(EnergyBlocks.SOLID_FUEL_GENERATORS.values())));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BatteryBlockEntity>> BATTERY =
            BLOCK_ENTITY_TYPES.register(
                    "battery",
                    () -> new BlockEntityType<>(BatteryBlockEntity::new, blocks(EnergyBlocks.BATTERIES.values())));

    private EnergyBlockEntityTypes() {}

    /** Adds the block entity types to their registry. */
    public static void register(IEventBus modBus) {
        BLOCK_ENTITY_TYPES.register(modBus);
    }

    private static Set<Block> blocks(Collection<? extends Supplier<? extends Block>> holders) {
        return holders.stream().map(Supplier::get).collect(Collectors.toUnmodifiableSet());
    }
}
