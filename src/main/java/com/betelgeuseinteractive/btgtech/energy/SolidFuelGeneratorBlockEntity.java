package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.core.BalanceConfig;
import com.betelgeuseinteractive.btgtech.core.machine.MachineBlockEntity;
import com.betelgeuseinteractive.btgtech.core.machine.MachineEnergyStorage;
import com.betelgeuseinteractive.btgtech.core.machine.MachineItemSlots;
import com.betelgeuseinteractive.btgtech.energy.logic.FuelBurner;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;

/**
 * The solid fuel generator's block entity: while its redstone mode allows it and its buffer has room, it burns one fuel
 * item at a time and produces its tier's output each tick (balance.md, section 2).
 */
public final class SolidFuelGeneratorBlockEntity extends MachineBlockEntity implements MenuProvider {
    static final int FUEL_SLOT = 0;

    private final FuelBurner burner = new FuelBurner();

    public SolidFuelGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(EnergyBlockEntityTypes.SOLID_FUEL_GENERATOR.get(), pos, state);
    }

    /** Returns the energy of the burning fuel that is not produced yet. */
    int fuelRemaining() {
        return burner.remaining();
    }

    /** Returns the energy of the burning fuel when it started, or 0 when nothing burns. */
    int fuelTotal() {
        return burner.total();
    }

    @Override
    protected void tick(ServerLevel level) {
        MachineEnergyStorage energy = requireEnergy();
        int room = energy.capacity() - energy.stored();
        int produced = 0;
        if (room > 0 && redstoneAllowsOperation(level)) {
            if (!burner.isBurning()) {
                startNextFuel(level);
            }
            if (burner.isBurning()) {
                produced = energy.generate(burner.burn(BalanceConfig.SOLID_FUEL_GENERATOR_OUTPUT.get(tier()), room));
                setChanged();
            }
        }
        setActive(level, produced > 0);
    }

    // Takes one fuel item from the slot, leaving its remainder (an empty bucket for lava) when it was the last one.
    private void startNextFuel(ServerLevel level) {
        MachineItemSlots items = requireItems();
        ItemResource resource = items.getResource(FUEL_SLOT);
        int count = (int) items.getAmountAsLong(FUEL_SLOT);
        if (resource.isEmpty() || count == 0) {
            return;
        }
        ItemStack fuel = resource.toStack();
        int burnTime = SolidFuelGeneratorBlock.burnTime(fuel, level);
        if (burnTime <= 0) {
            return;
        }
        long fuelEnergy = (long) burnTime * BalanceConfig.SOLID_FUEL_ENERGY_PER_BURN_TICK.getAsInt();
        burner.start((int) Math.min(Integer.MAX_VALUE, fuelEnergy));
        ItemStackTemplate remainder = fuel.getCraftingRemainder();
        if (count > 1) {
            items.set(FUEL_SLOT, resource, count - 1);
        } else if (remainder != null) {
            ItemStack left = remainder.create();
            items.set(FUEL_SLOT, ItemResource.of(left), left.getCount());
        } else {
            items.set(FUEL_SLOT, ItemResource.EMPTY, 0);
        }
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new SolidFuelGeneratorMenu(containerId, inventory, this);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("fuel_remaining", burner.remaining());
        output.putInt("fuel_total", burner.total());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        burner.restore(input.getIntOr("fuel_remaining", 0), input.getIntOr("fuel_total", 0));
    }
}
