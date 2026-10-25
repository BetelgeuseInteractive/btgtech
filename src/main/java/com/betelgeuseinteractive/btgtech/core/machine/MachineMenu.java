package com.betelgeuseinteractive.btgtech.core.machine;

import com.betelgeuseinteractive.btgtech.core.Tier;
import com.betelgeuseinteractive.btgtech.core.machine.logic.RedstoneMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jspecify.annotations.Nullable;

/**
 * Base of every machine menu: the machine slots, the player inventory in the standard layout and the numbers the screen
 * shows (ADR 0044, 0055). Numbers travel as menu data slots, each a full 32-bit value under NeoForge (ADR 0073).
 *
 * <p>The server-side menu reads the block entity; the client-side menu starts empty and receives slots and numbers from
 * the server. Subclasses add their machine slots in index order with {@link #addMachineSlot}, then call
 * {@link #addPlayerInventory}.
 */
public abstract class MachineMenu extends AbstractContainerMenu {
    /** The menu button that switches to the next redstone mode (ADR 0055). */
    public static final int CYCLE_REDSTONE_MODE_BUTTON = 0;

    private static final int ENERGY = 0;
    private static final int ENERGY_CAPACITY = 1;
    private static final int PROGRESS = 2;
    private static final int DURATION = 3;
    private static final int REDSTONE_MODE = 4;
    private static final int DATA_SIZE = 5;
    private static final int PLAYER_SLOTS = 36;
    private static final RedstoneMode[] REDSTONE_MODES = RedstoneMode.values();

    private final @Nullable MachineBlockEntity machine;
    private final MachineBlock block;
    private final ContainerLevelAccess access;
    private final @Nullable MachineItemSlots items;
    private final ContainerData data;
    private int machineSlots;
    private int inputSlots;

    /** Creates the server-side menu of the machine. */
    protected MachineMenu(MenuType<?> type, int containerId, MachineBlockEntity machine) {
        super(type, containerId);
        this.machine = machine;
        this.block = (MachineBlock) machine.getBlockState().getBlock();
        this.access = ContainerLevelAccess.create(machine.getLevel(), machine.getBlockPos());
        this.items = machine.items();
        this.data = new MachineData(machine);
        addDataSlots(data);
    }

    /**
     * Creates the client-side menu of the machine at the position the server sent.
     *
     * @throws IllegalStateException if there is no machine block at the position
     */
    protected MachineMenu(MenuType<?> type, int containerId, Inventory inventory, BlockPos pos) {
        super(type, containerId);
        Level level = inventory.player.level();
        if (!(level.getBlockState(pos).getBlock() instanceof MachineBlock machineBlock)) {
            throw new IllegalStateException("No machine block at " + pos);
        }
        MachineDefinition definition = machineBlock.definition();
        this.machine = null;
        this.block = machineBlock;
        this.access = ContainerLevelAccess.create(level, pos);
        this.items = definition.slotCount() == 0
                ? null
                : new MachineItemSlots(
                        definition.inputSlots(),
                        definition.outputSlots(),
                        index -> {},
                        (slot, resource) -> machineBlock.acceptsInput(level, slot, resource));
        this.data = new SimpleContainerData(DATA_SIZE);
        addDataSlots(data);
    }

    /**
     * Adds the machine slot with the given index at the given screen position.
     *
     * @throws IllegalStateException if the machine has no slots or the slots are not added in index order
     */
    protected void addMachineSlot(int index, int x, int y) {
        if (items == null || index != machineSlots) {
            throw new IllegalStateException("Machine slots must be added in index order, expected " + machineSlots);
        }
        if (items.isInput(index)) {
            addSlot(new ResourceHandlerSlot(items, items::set, index, x, y));
            inputSlots++;
        } else {
            addSlot(new OutputSlot(items, index, x, y));
        }
        machineSlots++;
    }

    /** Adds the player inventory and hotbar at the standard position of a 176 × 166 screen (gui.md). */
    protected void addPlayerInventory(Inventory inventory) {
        addStandardInventorySlots(inventory, 8, 84);
    }

    /** Returns the tier of the machine. */
    public Tier tier() {
        return block.tier();
    }

    /** Returns whether the machine stores energy, which decides whether its screen shows the energy bar. */
    public boolean hasEnergy() {
        return block.definition().energy() != null;
    }

    /** Returns the stored energy. */
    public int energy() {
        return data.get(ENERGY);
    }

    /** Returns the energy capacity, or 0 for machines without energy. */
    public int energyCapacity() {
        return data.get(ENERGY_CAPACITY);
    }

    /** Returns the ticks done in the current operation. */
    public int progress() {
        return data.get(PROGRESS);
    }

    /** Returns the length of the current operation in ticks, or 0 when idle. */
    public int duration() {
        return data.get(DURATION);
    }

    /** Returns the redstone mode. */
    public RedstoneMode redstoneMode() {
        return REDSTONE_MODES[Math.floorMod(data.get(REDSTONE_MODE), REDSTONE_MODES.length)];
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (buttonId == CYCLE_REDSTONE_MODE_BUTTON && machine != null) {
            machine.cycleRedstoneMode();
            return true;
        }
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = slots.get(slotIndex);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        // Machine slots go to the player inventory; player items go to the input slots.
        boolean moved = slotIndex < machineSlots
                ? moveItemStackTo(stack, machineSlots, machineSlots + PLAYER_SLOTS, true)
                : moveItemStackTo(stack, 0, inputSlots, false);
        if (!moved) {
            return ItemStack.EMPTY;
        }
        slot.setByPlayer(stack.isEmpty() ? ItemStack.EMPTY : stack);
        slot.onTake(player, stack);
        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, block);
    }

    private static final class OutputSlot extends ResourceHandlerSlot {
        OutputSlot(MachineItemSlots items, int index, int x, int y) {
            super(items, items::set, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    private static final class MachineData implements ContainerData {
        private final MachineBlockEntity machine;

        MachineData(MachineBlockEntity machine) {
            this.machine = machine;
        }

        @Override
        public int get(int index) {
            MachineEnergyStorage energy = machine.energy();
            return switch (index) {
                case ENERGY -> energy == null ? 0 : energy.stored();
                case ENERGY_CAPACITY -> energy == null ? 0 : energy.capacity();
                case PROGRESS -> machine.progress().progress();
                case DURATION -> machine.progress().duration();
                case REDSTONE_MODE -> machine.redstoneMode().ordinal();
                default -> 0;
            };
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
