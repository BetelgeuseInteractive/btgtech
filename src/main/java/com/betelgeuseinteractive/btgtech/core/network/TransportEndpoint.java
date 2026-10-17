package com.betelgeuseinteractive.btgtech.core.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jspecify.annotations.Nullable;

/**
 * A block next to a network node that the network exchanges resources with, reached through a cached capability lookup
 * (ADR 0026).
 *
 * @param <C> the endpoint capability
 */
public final class TransportEndpoint<C> {
    private final BlockPos node;
    private final Direction side;
    private final BlockCapabilityCache<C, Direction> capability;

    TransportEndpoint(BlockPos node, Direction side, BlockCapabilityCache<C, Direction> capability) {
        this.node = node;
        this.side = side;
        this.capability = capability;
    }

    /** Returns the position of the node the endpoint is attached to. */
    public BlockPos node() {
        return node;
    }

    /** Returns the side of the node that faces the endpoint. */
    public Direction side() {
        return side;
    }

    /** Returns the endpoint's handler, or {@code null} if the block is not loaded or does not expose the capability. */
    public @Nullable C handler() {
        return capability.getCapability();
    }
}
