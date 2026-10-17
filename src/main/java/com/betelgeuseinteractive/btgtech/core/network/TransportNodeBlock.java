package com.betelgeuseinteractive.btgtech.core.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A block whose placed instances are transport network nodes, such as a cable or a pipe. Its connections live in the
 * block state (ADR 0028, 0030); the block calls {@link TransportNetworks#refresh(ServerLevel, BlockPos)} whenever its
 * state or presence changes.
 */
public interface TransportNodeBlock {
    /** Returns the network type the node belongs to. */
    TransportNetworkType<?> networkType(BlockState state);

    /** Returns whether the node connects to a node of the same type on the given side. */
    boolean connectsToNode(BlockState state, Direction side);

    /** Returns whether the node treats the block on the given side as an endpoint. */
    boolean connectsToEndpoint(BlockState state, Direction side);
}
