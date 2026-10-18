package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.core.machine.MachineBlockEntity;
import com.betelgeuseinteractive.btgtech.core.machine.MachineEnergyStorage;
import com.betelgeuseinteractive.btgtech.energy.logic.ComparatorSignal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.energy.DelegatingEnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

/**
 * The battery's block entity: exposes its storage only while the redstone mode allows it, shows the active front while
 * energy flows, and keeps its comparator output up to date.
 */
public final class BatteryBlockEntity extends MachineBlockEntity implements MenuProvider {
    /** Ticks without a transfer after which the battery counts as idle again. */
    static final int ACTIVE_TIMEOUT = 20;

    private final EnergyHandler redstoneControlledEnergy;
    private long lastTransferTick = Long.MIN_VALUE;
    private int comparatorSignal = -1;

    public BatteryBlockEntity(BlockPos pos, BlockState state) {
        super(EnergyBlockEntityTypes.BATTERY.get(), pos, state);
        this.redstoneControlledEnergy = new RedstoneControlledEnergy(storage());
    }

    @Override
    public @Nullable EnergyHandler energyHandler(@Nullable Direction side) {
        return super.energyHandler(side) == null ? null : redstoneControlledEnergy;
    }

    /** Returns the comparator output, proportional to the fill level. */
    public int comparatorSignal() {
        MachineEnergyStorage energy = storage();
        return ComparatorSignal.of(energy.stored(), energy.capacity());
    }

    @Override
    protected void onEnergyChanged() {
        super.onEnergyChanged();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        int signal = comparatorSignal();
        if (signal != comparatorSignal) {
            comparatorSignal = signal;
            serverLevel.updateNeighbourForOutputSignal(
                    worldPosition, getBlockState().getBlock());
        }
        lastTransferTick = serverLevel.getGameTime();
        setActive(serverLevel, true);
        scheduleActiveCheck(serverLevel);
    }

    /** Ends the active state once no energy has flowed for {@link #ACTIVE_TIMEOUT} ticks. */
    void updateActiveState(ServerLevel level) {
        setActive(level, level.getGameTime() - lastTransferTick < ACTIVE_TIMEOUT);
        if (getBlockState().getValue(BatteryBlock.ACTIVE)) {
            scheduleActiveCheck(level);
        }
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new BatteryMenu(containerId, inventory, this);
    }

    private void scheduleActiveCheck(ServerLevel level) {
        if (!level.getBlockTicks()
                .hasScheduledTick(worldPosition, getBlockState().getBlock())) {
            level.scheduleTick(worldPosition, getBlockState().getBlock(), ACTIVE_TIMEOUT);
        }
    }

    private MachineEnergyStorage storage() {
        MachineEnergyStorage energy = energy();
        if (energy == null) {
            throw new IllegalStateException("A battery always has an energy storage");
        }
        return energy;
    }

    // Transfers only while the redstone mode allows operation; reading the stored energy is always possible.
    private final class RedstoneControlledEnergy extends DelegatingEnergyHandler {
        RedstoneControlledEnergy(EnergyHandler storage) {
            super(storage);
        }

        @Override
        public int insert(int amount, TransactionContext transaction) {
            return transfersAllowed() ? super.insert(amount, transaction) : 0;
        }

        @Override
        public int extract(int amount, TransactionContext transaction) {
            return transfersAllowed() ? super.extract(amount, transaction) : 0;
        }

        private boolean transfersAllowed() {
            return level != null && redstoneMode().allowsOperation(level.hasNeighborSignal(worldPosition));
        }
    }
}
