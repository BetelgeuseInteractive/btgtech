package com.betelgeuseinteractive.btgtech.core.network;

import com.betelgeuseinteractive.btgtech.core.network.logic.Network;
import com.betelgeuseinteractive.btgtech.core.network.logic.NetworkGraph;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

/**
 * The transport networks of one server level (ADR 0028, 0031).
 *
 * <p>Networks live only in memory: nodes are registered from block states when their chunk loads or their block
 * changes, and dropped when their chunk unloads. Endpoint lists are cached per network and rebuilt only when the
 * network changes. Used on the server thread only.
 */
public final class TransportNetworkManager {
    private static final Predicate<BlockState> IS_NODE = state -> state.getBlock() instanceof TransportNodeBlock;

    private final ServerLevel level;
    private final Map<TransportNetworkType<?>, NetworkGraph<BlockPos>> graphs = new LinkedHashMap<>();
    private final Map<BlockPos, Node> nodes = new HashMap<>();
    private final Long2ObjectMap<Set<BlockPos>> nodesByChunk = new Long2ObjectOpenHashMap<>();
    private final Map<Network<BlockPos>, CachedNetwork> cachedNetworks = new IdentityHashMap<>();
    // Block changes caused by transfers are applied after the tick, so networks never change while they are iterated.
    private final Set<BlockPos> deferredRefreshes = new LinkedHashSet<>();
    private boolean ticking;

    TransportNetworkManager(ServerLevel level) {
        this.level = level;
    }

    /** Re-reads the block at the position and adds, updates or removes its node. */
    public void refresh(BlockPos pos) {
        if (ticking) {
            deferredRefreshes.add(pos.immutable());
        } else {
            refresh(pos.immutable(), level.getBlockState(pos));
        }
    }

    void tick() {
        ticking = true;
        try {
            cachedNetworks.keySet().removeIf(Network::isRemoved);
            for (Map.Entry<TransportNetworkType<?>, NetworkGraph<BlockPos>> entry : graphs.entrySet()) {
                for (Network<BlockPos> network : entry.getValue().networks()) {
                    transfer(entry.getKey(), network);
                }
            }
        } finally {
            ticking = false;
        }
        if (!deferredRefreshes.isEmpty()) {
            List<BlockPos> pending = List.copyOf(deferredRefreshes);
            deferredRefreshes.clear();
            pending.forEach(this::refresh);
        }
    }

    void onChunkLoaded(LevelChunk chunk) {
        LevelChunkSection[] sections = chunk.getSections();
        int baseX = SectionPos.sectionToBlockCoord(chunk.getPos().x());
        int baseZ = SectionPos.sectionToBlockCoord(chunk.getPos().z());
        for (int index = 0; index < sections.length; index++) {
            LevelChunkSection section = sections[index];
            // The palette check skips almost every section without reading its blocks.
            if (section.hasOnlyAir() || !section.maybeHas(IS_NODE)) {
                continue;
            }
            int baseY = SectionPos.sectionToBlockCoord(chunk.getSectionYFromSectionIndex(index));
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    for (int x = 0; x < 16; x++) {
                        BlockState state = section.getBlockState(x, y, z);
                        if (IS_NODE.test(state)) {
                            refresh(new BlockPos(baseX + x, baseY + y, baseZ + z), state);
                        }
                    }
                }
            }
        }
    }

    void onChunkUnloaded(LevelChunk chunk) {
        Set<BlockPos> inChunk = nodesByChunk.remove(chunk.getPos().pack());
        if (inChunk == null) {
            return;
        }
        for (BlockPos pos : inChunk) {
            Node node = nodes.remove(pos);
            graphs.get(node.type()).remove(pos);
        }
    }

    private void refresh(BlockPos pos, BlockState state) {
        Node old = nodes.get(pos);
        if (!(state.getBlock() instanceof TransportNodeBlock block)) {
            if (old != null) {
                removeNode(pos, old);
            }
            return;
        }
        TransportNetworkType<?> type = block.networkType(state);
        if (old != null && old.type() != type) {
            removeNode(pos, old);
            old = null;
        }
        NetworkGraph<BlockPos> graph = graphs.computeIfAbsent(type, unused -> new NetworkGraph<>());
        if (old == null) {
            graph.add(pos);
            nodesByChunk
                    .computeIfAbsent(ChunkPos.pack(pos), unused -> new HashSet<>())
                    .add(pos);
        }

        Set<Direction> nodeSides = EnumSet.noneOf(Direction.class);
        Set<Direction> endpointSides = EnumSet.noneOf(Direction.class);
        for (Direction side : Direction.values()) {
            if (block.connectsToNode(state, side)) {
                nodeSides.add(side);
            }
            if (block.connectsToEndpoint(state, side)) {
                endpointSides.add(side);
            }
        }
        boolean endpointsChanged = old == null || !old.endpointSides().equals(endpointSides);
        List<TransportEndpoint<?>> endpoints =
                endpointsChanged ? createEndpoints(type, pos, endpointSides) : old.endpoints();
        nodes.put(pos, new Node(type, nodeSides, endpointSides, endpoints));

        // Two nodes connect only when both of them connect toward each other.
        for (Direction side : Direction.values()) {
            BlockPos other = pos.relative(side);
            Node neighbor = nodes.get(other);
            if (neighbor == null || neighbor.type() != type) {
                continue;
            }
            if (nodeSides.contains(side) && neighbor.nodeSides().contains(side.getOpposite())) {
                graph.connect(pos, other);
            } else {
                graph.disconnect(pos, other);
            }
        }
        if (endpointsChanged) {
            cachedNetworks.remove(graph.networkOf(pos));
        }
    }

    private void removeNode(BlockPos pos, Node node) {
        graphs.get(node.type()).remove(pos);
        nodes.remove(pos);
        long chunk = ChunkPos.pack(pos);
        Set<BlockPos> inChunk = nodesByChunk.get(chunk);
        if (inChunk != null) {
            inChunk.remove(pos);
            if (inChunk.isEmpty()) {
                nodesByChunk.remove(chunk);
            }
        }
    }

    private <C> List<TransportEndpoint<?>> createEndpoints(
            TransportNetworkType<C> type, BlockPos pos, Set<Direction> sides) {
        List<TransportEndpoint<?>> endpoints = new ArrayList<>(sides.size());
        for (Direction side : sides) {
            BlockCapabilityCache<C, Direction> capability = BlockCapabilityCache.create(
                    type.endpointCapability(), level, pos.relative(side), side.getOpposite());
            endpoints.add(new TransportEndpoint<>(pos, side, capability));
        }
        return List.copyOf(endpoints);
    }

    private <C> void transfer(TransportNetworkType<C> type, Network<BlockPos> network) {
        CachedNetwork cached = cachedNetworks.get(network);
        if (cached == null || cached.revision() != network.revision()) {
            cached = new CachedNetwork(network.revision(), new TransportNetwork<>(level, endpointsOf(type, network)));
            cachedNetworks.put(network, cached);
        }
        // Every endpoint of a network was created from the capability of that network's type.
        @SuppressWarnings("unchecked")
        TransportNetwork<C> view = (TransportNetwork<C>) cached.view();
        if (!view.endpoints().isEmpty()) {
            type.transfer(view);
        }
    }

    @SuppressWarnings("unchecked")
    private <C> List<TransportEndpoint<C>> endpointsOf(TransportNetworkType<C> type, Network<BlockPos> network) {
        // Sorted, so that strategies see the same order on every tick and after every rebuild.
        List<BlockPos> members = new ArrayList<>(network.members());
        members.sort(null);
        List<TransportEndpoint<C>> endpoints = new ArrayList<>();
        for (BlockPos pos : members) {
            for (TransportEndpoint<?> endpoint : nodes.get(pos).endpoints()) {
                endpoints.add((TransportEndpoint<C>) endpoint);
            }
        }
        return endpoints;
    }

    private record Node(
            TransportNetworkType<?> type,
            Set<Direction> nodeSides,
            Set<Direction> endpointSides,
            List<TransportEndpoint<?>> endpoints) {}

    private record CachedNetwork(int revision, TransportNetwork<?> view) {}
}
