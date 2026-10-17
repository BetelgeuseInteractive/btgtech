package com.betelgeuseinteractive.btgtech.core.network;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;

/**
 * One kind of transport network, for example the energy cables of one tier. Nodes connect only to nodes of the same
 * type; the feature that owns the network supplies the transfer strategy (ADR 0031).
 *
 * @param <C> the capability that endpoints expose, for example an energy handler
 */
public interface TransportNetworkType<C> {
    /** Returns the capability that endpoints expose on the face that touches the network. */
    BlockCapability<C, Direction> endpointCapability();

    /** Moves resources between the endpoints of one network; called once per server tick for every network. */
    void transfer(TransportNetwork<C> network);
}
