package com.betelgeuseinteractive.btgtech.core.network.logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Undirected graph of transport nodes, partitioned into connected {@link Network}s (ADR 0028, 0031).
 *
 * <p>Changes are incremental: connecting two networks moves the members of the smaller one into the larger one, and a
 * removal searches only the network it happened in. Node identity follows {@code equals} and {@code hashCode}.
 *
 * @param <N> the node type, for example a block position
 */
public final class NetworkGraph<N> {
    private final Map<N, Set<N>> adjacency = new HashMap<>();
    private final Map<N, Network<N>> membership = new HashMap<>();
    private final Set<Network<N>> networks = new LinkedHashSet<>();
    private final Collection<Network<N>> networksView = Collections.unmodifiableSet(networks);

    /**
     * Adds a node without connections; it forms a new network.
     *
     * @throws IllegalArgumentException if the node is already present
     */
    public Network<N> add(N node) {
        if (adjacency.containsKey(node)) {
            throw new IllegalArgumentException("Node already present: " + node);
        }
        adjacency.put(node, new HashSet<>());
        Network<N> network = new Network<>();
        network.add(node);
        membership.put(node, network);
        networks.add(network);
        return network;
    }

    /**
     * Connects two nodes and merges their networks if they differ. Connecting connected nodes has no effect.
     *
     * @throws IllegalArgumentException if a node is missing or both nodes are the same
     */
    public void connect(N first, N second) {
        requirePresent(first);
        requirePresent(second);
        if (first.equals(second)) {
            throw new IllegalArgumentException("A node cannot connect to itself: " + first);
        }
        if (!adjacency.get(first).add(second)) {
            return;
        }
        adjacency.get(second).add(first);
        Network<N> firstNetwork = membership.get(first);
        Network<N> secondNetwork = membership.get(second);
        if (firstNetwork != secondNetwork) {
            merge(firstNetwork, secondNetwork);
        }
    }

    /**
     * Removes the connection between two nodes and splits their network if no other path joins them. Disconnecting
     * unconnected nodes has no effect.
     *
     * @throws IllegalArgumentException if a node is missing
     */
    public void disconnect(N first, N second) {
        requirePresent(first);
        requirePresent(second);
        if (!adjacency.get(first).remove(second)) {
            return;
        }
        adjacency.get(second).remove(first);
        splitIfDisconnected(membership.get(first), List.of(first, second));
    }

    /**
     * Removes a node and its connections, splitting its network into every part that is no longer connected.
     *
     * @return whether the node was present
     */
    public boolean remove(N node) {
        Set<N> neighbors = adjacency.remove(node);
        if (neighbors == null) {
            return false;
        }
        for (N neighbor : neighbors) {
            adjacency.get(neighbor).remove(node);
        }
        Network<N> network = membership.remove(node);
        network.remove(node);
        if (network.size() == 0) {
            network.markRemoved();
            networks.remove(network);
        } else {
            splitIfDisconnected(network, new ArrayList<>(neighbors));
        }
        return true;
    }

    /** Returns whether the node is present. */
    public boolean contains(N node) {
        return adjacency.containsKey(node);
    }

    /**
     * Returns the network the node belongs to.
     *
     * @throws IllegalArgumentException if the node is missing
     */
    public Network<N> networkOf(N node) {
        requirePresent(node);
        return membership.get(node);
    }

    /**
     * Returns a read-only view of the nodes connected to the given node.
     *
     * @throws IllegalArgumentException if the node is missing
     */
    public Set<N> neighbors(N node) {
        requirePresent(node);
        return Collections.unmodifiableSet(adjacency.get(node));
    }

    /** Returns a read-only view of the current networks, in creation order. */
    public Collection<Network<N>> networks() {
        return networksView;
    }

    private void requirePresent(N node) {
        if (!adjacency.containsKey(node)) {
            throw new IllegalArgumentException("Unknown node: " + node);
        }
    }

    private void merge(Network<N> first, Network<N> second) {
        Network<N> kept = first.size() >= second.size() ? first : second;
        Network<N> absorbed = kept == first ? second : first;
        for (N node : absorbed.members()) {
            membership.put(node, kept);
        }
        kept.addAll(absorbed.members());
        absorbed.markRemoved();
        networks.remove(absorbed);
    }

    // Finds the parts reachable from the seeds; the largest part stays in the original network.
    private void splitIfDisconnected(Network<N> network, List<N> seeds) {
        if (seeds.size() < 2) {
            return;
        }
        Set<N> visited = new HashSet<>();
        List<Set<N>> parts = new ArrayList<>();
        for (N seed : seeds) {
            if (visited.contains(seed)) {
                continue;
            }
            Set<N> part = reachableFrom(seed, visited);
            if (part.size() == network.size()) {
                return;
            }
            parts.add(part);
        }
        Set<N> largest = parts.stream().max(Comparator.comparingInt(Set::size)).orElseThrow();
        for (Set<N> part : parts) {
            if (part == largest) {
                continue;
            }
            network.removeAll(part);
            Network<N> split = new Network<>();
            split.addAll(part);
            for (N node : part) {
                membership.put(node, split);
            }
            networks.add(split);
        }
    }

    private Set<N> reachableFrom(N start, Set<N> visited) {
        Set<N> part = new HashSet<>();
        ArrayDeque<N> queue = new ArrayDeque<>();
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            N node = queue.poll();
            part.add(node);
            for (N neighbor : adjacency.get(node)) {
                if (visited.add(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }
        return part;
    }
}
