package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.core.BalanceConfig;
import com.betelgeuseinteractive.btgtech.core.Tier;
import com.betelgeuseinteractive.btgtech.core.machine.MachineBlock;
import com.betelgeuseinteractive.btgtech.core.machine.MachineBlockEntity;
import com.betelgeuseinteractive.btgtech.core.machine.MachineDefinition;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A battery (ADR 0039): stores energy, passive (it never pushes, ADR 0041), comparator output proportional to its fill
 * level. Energy flows in and out through its energy handler within the tier's transfer limit.
 */
public final class BatteryBlock extends MachineBlock {
    static final MachineDefinition DEFINITION = new MachineDefinition(
            0,
            0,
            new MachineDefinition.EnergyLimits(
                    BalanceConfig.BATTERY_CAPACITY::get,
                    BalanceConfig.BATTERY_TRANSFER::get,
                    BalanceConfig.BATTERY_TRANSFER::get));

    private static final MapCodec<BatteryBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    propertiesCodec(), TIER_CODEC.fieldOf("tier").forGetter(BatteryBlock::tier))
            .apply(instance, BatteryBlock::new));

    BatteryBlock(Properties properties, Tier tier) {
        super(properties, tier, DEFINITION);
    }

    @Override
    protected MapCodec<BatteryBlock> codec() {
        return CODEC;
    }

    @Override
    protected BlockEntityType<? extends MachineBlockEntity> blockEntityType() {
        return EnergyBlockEntityTypes.BATTERY.get();
    }

    @Override
    protected boolean ticksOnServer() {
        return false;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return level.getBlockEntity(pos) instanceof BatteryBlockEntity battery ? battery.comparatorSignal() : 0;
    }

    // Scheduled while energy flows, to end the active state once it stops.
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof BatteryBlockEntity battery) {
            battery.updateActiveState(level);
        }
    }
}
