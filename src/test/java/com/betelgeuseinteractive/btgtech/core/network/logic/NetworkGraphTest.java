package com.betelgeuseinteractive.btgtech.core.network.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NetworkGraphTest {
    private final NetworkGraph<String> graph = new NetworkGraph<>();

    @Test
    void addCreatesAnIsolatedNetwork() {
        Network<String> network = graph.add("a");

        assertEquals(Set.of("a"), network.members());
        assertEquals(1, network.size());
        assertFalse(network.isRemoved());
        assertSame(network, graph.networkOf("a"));
        assertEquals(List.of(network), List.copyOf(graph.networks()));
    }

    @Test
    void addRejectsANodeThatIsAlreadyPresent() {
        graph.add("a");

        assertThrows(IllegalArgumentException.class, () -> graph.add("a"));
    }

    @Test
    void connectMergesIntoTheLargerNetwork() {
        Network<String> larger = graph.add("a");
        graph.add("b");
        graph.connect("a", "b");
        Network<String> smaller = graph.add("c");
        int revision = larger.revision();

        graph.connect("b", "c");

        assertSame(larger, graph.networkOf("c"));
        assertEquals(Set.of("a", "b", "c"), larger.members());
        assertNotEquals(revision, larger.revision());
        assertTrue(smaller.isRemoved());
        assertEquals(0, smaller.size());
        assertEquals(List.of(larger), List.copyOf(graph.networks()));
    }

    @Test
    void connectInsideOneNetworkKeepsTheMembership() {
        Network<String> network = chain("a", "b", "c");
        int revision = network.revision();

        graph.connect("a", "c");

        assertEquals(revision, network.revision());
        assertEquals(Set.of("a", "b"), graph.neighbors("c"));
    }

    @Test
    void connectRejectsUnknownNodesAndSelfConnections() {
        graph.add("a");

        assertThrows(IllegalArgumentException.class, () -> graph.connect("a", "missing"));
        assertThrows(IllegalArgumentException.class, () -> graph.connect("a", "a"));
    }

    @Test
    void disconnectSplitsWhenNoOtherPathRemains() {
        Network<String> network = chain("a", "b", "c", "d");

        graph.disconnect("a", "b");

        assertSame(network, graph.networkOf("b"));
        assertEquals(Set.of("b", "c", "d"), network.members());
        Network<String> split = graph.networkOf("a");
        assertNotSame(network, split);
        assertEquals(Set.of("a"), split.members());
        assertEquals(2, graph.networks().size());
    }

    @Test
    void disconnectKeepsTheNetworkWhenAnotherPathExists() {
        Network<String> network = chain("a", "b", "c");
        graph.connect("c", "a");

        graph.disconnect("a", "b");

        assertSame(network, graph.networkOf("a"));
        assertSame(network, graph.networkOf("b"));
        assertEquals(1, graph.networks().size());
    }

    @Test
    void disconnectIgnoresNodesThatAreNotConnected() {
        graph.add("a");
        graph.add("b");

        graph.disconnect("a", "b");

        assertEquals(2, graph.networks().size());
        assertThrows(IllegalArgumentException.class, () -> graph.disconnect("a", "missing"));
    }

    @Test
    void removeSplitsIntoEveryDisconnectedPartAndKeepsTheLargest() {
        // Star around "hub": one arm of three nodes and two single-node arms.
        Network<String> network = chain("hub", "x1", "x2", "x3");
        graph.add("y");
        graph.connect("hub", "y");
        graph.add("z");
        graph.connect("hub", "z");

        assertTrue(graph.remove("hub"));

        assertFalse(graph.contains("hub"));
        assertSame(network, graph.networkOf("x1"));
        assertEquals(Set.of("x1", "x2", "x3"), network.members());
        assertEquals(Set.of("y"), graph.networkOf("y").members());
        assertEquals(Set.of("z"), graph.networkOf("z").members());
        assertEquals(3, graph.networks().size());
    }

    @Test
    void removeOfTheLastNodeRemovesItsNetwork() {
        Network<String> network = graph.add("a");

        assertTrue(graph.remove("a"));

        assertTrue(network.isRemoved());
        assertTrue(graph.networks().isEmpty());
        assertFalse(graph.remove("a"));
    }

    @Test
    void containsAndNeighborsReflectTheGraph() {
        chain("a", "b", "c");

        assertTrue(graph.contains("b"));
        assertFalse(graph.contains("missing"));
        assertEquals(Set.of("a", "c"), graph.neighbors("b"));
        assertThrows(
                UnsupportedOperationException.class, () -> graph.neighbors("b").add("d"));
        assertThrows(IllegalArgumentException.class, () -> graph.neighbors("missing"));
    }

    @Test
    void networkOfRejectsUnknownNodes() {
        assertThrows(IllegalArgumentException.class, () -> graph.networkOf("missing"));
    }

    @Test
    void networksAndMembersAreReadOnlyViews() {
        Network<String> network = graph.add("a");

        assertThrows(UnsupportedOperationException.class, () -> graph.networks().clear());
        assertThrows(
                UnsupportedOperationException.class, () -> network.members().add("b"));
    }

    @Test
    void randomChangesAlwaysMatchTheConnectedComponents() {
        Random random = new Random(20261017L);
        Map<Integer, Set<Integer>> edges = new HashMap<>();
        NetworkGraph<Integer> randomGraph = new NetworkGraph<>();
        for (int step = 0; step < 5000; step++) {
            int a = random.nextInt(40);
            int b = random.nextInt(40);
            int action = random.nextInt(10);
            if (!randomGraph.contains(a)) {
                randomGraph.add(a);
                edges.put(a, new HashSet<>());
            } else if (action < 2) {
                randomGraph.remove(a);
                for (Integer neighbor : edges.remove(a)) {
                    edges.get(neighbor).remove(a);
                }
            } else if (randomGraph.contains(b) && a != b && action < 7) {
                randomGraph.connect(a, b);
                edges.get(a).add(b);
                edges.get(b).add(a);
            } else if (randomGraph.contains(b) && a != b) {
                randomGraph.disconnect(a, b);
                edges.get(a).remove(b);
                edges.get(b).remove(a);
            }
            assertMatchesComponents(randomGraph, edges);
        }
    }

    private Network<String> chain(String... nodes) {
        Network<String> network = graph.add(nodes[0]);
        for (int i = 1; i < nodes.length; i++) {
            graph.add(nodes[i]);
            graph.connect(nodes[i - 1], nodes[i]);
        }
        return network;
    }

    private static void assertMatchesComponents(NetworkGraph<Integer> graph, Map<Integer, Set<Integer>> edges) {
        Set<Integer> seen = new HashSet<>();
        int components = 0;
        for (Integer start : edges.keySet()) {
            if (!seen.add(start)) {
                continue;
            }
            Set<Integer> component = new HashSet<>(Set.of(start));
            ArrayDeque<Integer> queue = new ArrayDeque<>(List.of(start));
            while (!queue.isEmpty()) {
                for (Integer neighbor : edges.get(queue.poll())) {
                    if (seen.add(neighbor)) {
                        component.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
            components++;
            Network<Integer> network = graph.networkOf(start);
            assertFalse(network.isRemoved());
            assertEquals(component, network.members());
        }
        assertEquals(components, graph.networks().size());
    }
}
