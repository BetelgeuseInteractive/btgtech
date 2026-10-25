package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.core.BalanceConfig;
import com.betelgeuseinteractive.btgtech.core.Tier;
import com.betelgeuseinteractive.btgtech.core.machine.MachineBlock;
import com.betelgeuseinteractive.btgtech.core.machine.MachineBlockEntity;
import com.betelgeuseinteractive.btgtech.core.machine.MachineDefinition;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.Nullable;

/**
 * A solid fuel generator (ADR 0039): burns items with a burn time in the {@code neoforge:furnace_fuels} data map. Its
 * one input slot takes fuel only; its energy can be taken but not inserted. It does not push energy yet (WP-204).
 */
public final class SolidFuelGeneratorBlock extends MachineBlock {
    static final MachineDefinition DEFINITION = new MachineDefinition(
            1,
            0,
            new MachineDefinition.EnergyLimits(
                    BalanceConfig.SOLID_FUEL_GENERATOR_BUFFER::get,
                    tier -> 0,
                    BalanceConfig.SOLID_FUEL_GENERATOR_EXTRACT::get));

    private static final MapCodec<SolidFuelGeneratorBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), TIER_CODEC.fieldOf("tier").forGetter(SolidFuelGeneratorBlock::tier))
                    .apply(instance, SolidFuelGeneratorBlock::new));

    SolidFuelGeneratorBlock(Properties properties, Tier tier) {
        super(properties, tier, DEFINITION);
    }

    /** Returns the burn time of the item in ticks, 0 if it is no fuel, as the vanilla furnace counts it. */
    static int burnTime(ItemStack stack, Level level) {
        return stack.getBurnTime(RecipeType.SMELTING, level.fuelValues());
    }

    @Override
    public boolean acceptsInput(@Nullable Level level, int slot, ItemResource resource) {
        return level == null || burnTime(resource.toStack(), level) > 0;
    }

    @Override
    protected MapCodec<SolidFuelGeneratorBlock> codec() {
        return CODEC;
    }

    @Override
    protected BlockEntityType<? extends MachineBlockEntity> blockEntityType() {
        return EnergyBlockEntityTypes.SOLID_FUEL_GENERATOR.get();
    }
}
