package com.betelgeuseinteractive.btgtech.core.network;

import java.util.List;
import net.minecraft.server.level.ServerLevel;

/**
 * The view of one network that a {@link TransportNetworkType} works with during a tick.
 *
 * @param <C> the endpoint capability
 */
public final class TransportNetwork<C> {
    private final ServerLevel level;
    private final List<TransportEndpoint<C>> endpoints;

    TransportNetwork(ServerLevel level, List<TransportEndpoint<C>> endpoints) {
        this.level = level;
        this.endpoints = List.copyOf(endpoints);
    }

    /** Returns the level the network is in. */
    public ServerLevel level() {
        return level;
    }

    /** Returns the endpoints of all nodes, in a stable order; handlers may be unavailable (see {@code handler()}). */
    public List<TransportEndpoint<C>> endpoints() {
        return endpoints;
    }
}
