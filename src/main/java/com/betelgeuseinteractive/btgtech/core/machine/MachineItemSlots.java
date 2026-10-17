package com.betelgeuseinteractive.btgtech.core.machine;

import java.util.function.IntConsumer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * The item slots of a machine: input slots first, then output slots (ADR 0035). The machine itself has full access;
 * other blocks use {@link #sidedView()}, which applies the slot roles.
 */
public final class MachineItemSlots extends ItemStacksResourceHandler {
    private final int inputSlots;
    private final IntConsumer onChange;
    private final ResourceHandler<ItemResource> sidedView = new SidedView();

    /**
     * Creates empty slots.
     *
     * @param onChange called with the slot index after every change
     */
    public MachineItemSlots(int inputSlots, int outputSlots, IntConsumer onChange) {
        super(inputSlots + outputSlots);
        this.inputSlots = inputSlots;
        this.onChange = onChange;
    }

    /** Returns whether the slot at the index is an input slot. */
    public boolean isInput(int index) {
        return index < inputSlots;
    }

    /** Returns the view for other blocks: insertion into input slots only, extraction from output slots only. */
    public ResourceHandler<ItemResource> sidedView() {
        return sidedView;
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        onChange.accept(index);
    }

    private final class SidedView implements ResourceHandler<ItemResource> {
        @Override
        public int size() {
            return MachineItemSlots.this.size();
        }

        @Override
        public ItemResource getResource(int index) {
            return MachineItemSlots.this.getResource(index);
        }

        @Override
        public long getAmountAsLong(int index) {
            return MachineItemSlots.this.getAmountAsLong(index);
        }

        @Override
        public long getCapacityAsLong(int index, ItemResource resource) {
            return MachineItemSlots.this.getCapacityAsLong(index, resource);
        }

        @Override
        public boolean isValid(int index, ItemResource resource) {
            return isInput(index) && MachineItemSlots.this.isValid(index, resource);
        }

        @Override
        public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
            return isInput(index) ? MachineItemSlots.this.insert(index, resource, amount, transaction) : 0;
        }

        @Override
        public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
            return isInput(index) ? 0 : MachineItemSlots.this.extract(index, resource, amount, transaction);
        }
    }
}
