package com.betelgeuseinteractive.btgtech.core.machine;

import com.betelgeuseinteractive.btgtech.core.Tier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.Nullable;

/**
 * Base of every machine-like block: one block per tier (ADR 0033), a front that faces the player when placed and an
 * {@code active} state for the animated front (ADR 0035, 0047). Right-clicking opens the GUI when the block entity
 * provides a menu (ADR 0046).
 */
public abstract class MachineBlock extends BaseEntityBlock {
    /** The direction the front faces. */
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    /** Whether the machine is visibly working. */
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    /** Encodes a tier by its identifier, for the block codecs of the machine blocks. */
    protected static final Codec<Tier> TIER_CODEC = Codec.STRING.comapFlatMap(
            id -> Tier.byId(id).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown tier " + id)),
            Tier::id);

    private final Tier tier;
    private final MachineDefinition definition;

    protected MachineBlock(Properties properties, Tier tier, MachineDefinition definition) {
        super(properties);
        this.tier = tier;
        this.definition = definition;
        registerDefaultState(
                stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVE, false));
    }

    /** Returns the tier of this block. */
    public Tier tier() {
        return tier;
    }

    /** Returns the definition of this block. */
    public MachineDefinition definition() {
        return definition;
    }

    /** Returns the block entity type of this block. */
    protected abstract BlockEntityType<? extends MachineBlockEntity> blockEntityType();

    /**
     * Returns whether the input slot with the given index accepts the resource, for example only fuel in a generator's
     * fuel slot; it applies to players, other blocks and the machine alike. Accepts everything by default.
     *
     * @param level the level of the machine, or {@code null} while it has none, when everything is accepted
     */
    public boolean acceptsInput(@Nullable Level level, int slot, ItemResource resource) {
        return true;
    }

    /** Returns whether the block entity ticks on the server; passive blocks such as batteries return {@code false}. */
    protected boolean ticksOnServer() {
        return true;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return blockEntityType().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() || !ticksOnServer() ? null : serverTicker(type, blockEntityType());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof MenuProvider menu)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            player.openMenu(menu, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    private static <T extends BlockEntity, M extends MachineBlockEntity> @Nullable BlockEntityTicker<T> serverTicker(
            BlockEntityType<T> type, BlockEntityType<M> machineType) {
        return createTickerHelper(type, machineType, (level, pos, state, machine) -> machine.tick((ServerLevel) level));
    }
}
