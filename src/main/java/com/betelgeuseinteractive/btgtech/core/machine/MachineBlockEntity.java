package com.betelgeuseinteractive.btgtech.core.machine;

import com.betelgeuseinteractive.btgtech.core.Tier;
import com.betelgeuseinteractive.btgtech.core.machine.MachineDefinition.EnergyLimits;
import com.betelgeuseinteractive.btgtech.core.machine.logic.ActiveState;
import com.betelgeuseinteractive.btgtech.core.machine.logic.ProcessingProgress;
import com.betelgeuseinteractive.btgtech.core.machine.logic.RedstoneMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.Nullable;

/**
 * Base of every machine-like block entity (ADR 0034): components from the block's {@link MachineDefinition}, face
 * states, redstone mode, processing progress and the visible active state, all saved with the block entity.
 *
 * <p>The tier and the definition come from the {@link MachineBlock}, so one block entity type can serve all four tier
 * blocks. Subclasses are at most two levels deep and contain only behavior components cannot express.
 */
public abstract class MachineBlockEntity extends BlockEntity {
    private final Tier tier;
    private final FaceStates faces;
    private final @Nullable MachineEnergyStorage energy;
    private final @Nullable MachineItemSlots items;
    private final ProcessingProgress progress = new ProcessingProgress();
    private final ActiveState activeState = new ActiveState();
    private RedstoneMode redstoneMode = RedstoneMode.IGNORE;
    private boolean inputsChanged = true;

    protected MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        if (!(state.getBlock() instanceof MachineBlock block)) {
            throw new IllegalStateException("A machine block entity needs a machine block, found " + state);
        }
        MachineDefinition definition = block.definition();
        this.tier = block.tier();
        this.faces = FaceStates.withClosedFront(state.getValue(MachineBlock.FACING));
        EnergyLimits limits = definition.energy();
        this.energy = limits == null
                ? null
                : new MachineEnergyStorage(
                        limits.capacity().applyAsInt(tier),
                        limits.maxInsert().applyAsInt(tier),
                        limits.maxExtract().applyAsInt(tier),
                        this::onEnergyChanged);
        this.items = definition.slotCount() == 0
                ? null
                : new MachineItemSlots(
                        definition.inputSlots(),
                        definition.outputSlots(),
                        this::onSlotChanged,
                        (slot, resource) -> block.acceptsInput(level, slot, resource));
        this.activeState.restore(state.getValue(MachineBlock.ACTIVE));
    }

    /** Runs once per server tick if the block ticks (see {@link MachineBlock#ticksOnServer()}); does nothing by default. */
    protected void tick(ServerLevel level) {}

    /**
     * Called after every change of the stored energy through transfers, production or use, not when it is loaded; marks
     * the block entity as changed by default.
     */
    protected void onEnergyChanged() {
        setChanged();
    }

    /** Returns the tier of the block. */
    public Tier tier() {
        return tier;
    }

    /** Returns the direction the front faces. */
    public Direction front() {
        return getBlockState().getValue(MachineBlock.FACING);
    }

    /** Returns the energy storage, or {@code null} if the block has no energy. */
    public @Nullable MachineEnergyStorage energy() {
        return energy;
    }

    /** Returns the item slots with full access, or {@code null} if the block has no slots. */
    public @Nullable MachineItemSlots items() {
        return items;
    }

    /** Returns the progress of the current operation. */
    public ProcessingProgress progress() {
        return progress;
    }

    /** Returns the energy handler exposed on the given side, or {@code null} if there is none or the face is closed. */
    public @Nullable EnergyHandler energyHandler(@Nullable Direction side) {
        return energy != null && isAccessible(side) ? energy : null;
    }

    /** Returns the item handler exposed on the given side, or {@code null} if there is none or the face is closed. */
    public @Nullable ResourceHandler<ItemResource> itemHandler(@Nullable Direction side) {
        return items != null && isAccessible(side) ? items.sidedView() : null;
    }

    /** Returns whether the face on the given side is open. */
    public boolean isFaceOpen(Direction side) {
        return faces.isOpen(side);
    }

    /** Opens or closes the face on the given side; neighbors see the capability change at once. */
    public void toggleFace(Direction side) {
        faces.toggle(side);
        setChanged();
        if (level != null) {
            level.invalidateCapabilities(worldPosition);
        }
    }

    /** Returns the redstone mode. */
    public RedstoneMode redstoneMode() {
        return redstoneMode;
    }

    /** Switches to the next redstone mode. */
    public void cycleRedstoneMode() {
        redstoneMode = redstoneMode.next();
        setChanged();
    }

    /** Returns whether the redstone mode allows operating with the current signal. */
    protected boolean redstoneAllowsOperation(ServerLevel level) {
        return redstoneMode.allowsOperation(level.hasNeighborSignal(worldPosition));
    }

    /**
     * Returns whether an input slot changed since the last call; machines skip recipe lookups while nothing changed
     * (idle sleep, ADR 0035). The first call after creation or loading returns {@code true}.
     */
    protected boolean consumeInputChange() {
        boolean changed = inputsChanged;
        inputsChanged = false;
        return changed;
    }

    /** Requests the visible active state; the block state follows with the delay of {@link ActiveState}. */
    protected void setActive(ServerLevel level, boolean active) {
        boolean visible = activeState.update(active, level.getGameTime());
        BlockState state = getBlockState();
        if (state.getValue(MachineBlock.ACTIVE) != visible) {
            level.setBlock(worldPosition, state.setValue(MachineBlock.ACTIVE, visible), Block.UPDATE_ALL);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (energy != null) {
            energy.serialize(output.child("energy"));
        }
        if (items != null) {
            items.serialize(output.child("items"));
        }
        output.putInt("closed_faces", faces.closedMask());
        output.putString("redstone_mode", redstoneMode.id());
        output.putInt("progress", progress.progress());
        output.putInt("duration", progress.duration());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        if (energy != null) {
            input.child("energy").ifPresent(energy::deserialize);
        }
        if (items != null) {
            input.child("items").ifPresent(items::deserialize);
        }
        faces.restore(input.getIntOr("closed_faces", faces.closedMask()));
        redstoneMode = RedstoneMode.byId(input.getStringOr("redstone_mode", RedstoneMode.IGNORE.id()));
        progress.restore(input.getIntOr("progress", 0), input.getIntOr("duration", 0));
        inputsChanged = true;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        // Machine-like blocks drop themselves and spill their inventory (ADR 0052).
        if (items != null && level != null) {
            Containers.dropContents(level, pos, items.copyToList());
        }
    }

    private boolean isAccessible(@Nullable Direction side) {
        return side == null || faces.isOpen(side);
    }

    private void onSlotChanged(int index) {
        setChanged();
        if (items != null && items.isInput(index)) {
            inputsChanged = true;
        }
    }
}
