package com.betelgeuseinteractive.btgtech.core.machine;

import java.util.function.BiPredicate;
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
    private final BiPredicate<Integer, ItemResource> acceptsInput;
    private final ResourceHandler<ItemResource> sidedView = new SidedView();

    /**
     * Creates empty slots.
     *
     * @param onChange called with the slot index after every change
     * @param acceptsInput whether an input slot, by index, accepts a resource; output slots accept anything the
     *     machine puts there
     */
    public MachineItemSlots(
            int inputSlots, int outputSlots, IntConsumer onChange, BiPredicate<Integer, ItemResource> acceptsInput) {
        super(inputSlots + outputSlots);
        this.inputSlots = inputSlots;
        this.onChange = onChange;
        this.acceptsInput = acceptsInput;
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
    public boolean isValid(int index, ItemResource resource) {
        return !isInput(index) || acceptsInput.test(index, resource);
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
