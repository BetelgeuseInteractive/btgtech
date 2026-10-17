package com.betelgeuseinteractive.btgtech.core.network.logic;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A connected set of nodes in a {@link NetworkGraph}.
 *
 * <p>Network objects are reused: a merge keeps the larger network and removes the smaller one, and a split keeps the
 * largest part in the original network. The revision changes whenever the members change, so callers can cache data
 * derived from the members and rebuild it only when needed.
 *
 * @param <N> the node type
 */
public final class Network<N> {
    private final Set<N> members = new HashSet<>();
    private final Set<N> membersView = Collections.unmodifiableSet(members);
    private int revision;
    private boolean removed;

    Network() {}

    /** Returns a read-only view of the members that reflects later changes. */
    public Set<N> members() {
        return membersView;
    }

    /** Returns the number of members. */
    public int size() {
        return members.size();
    }

    /** Returns a counter that changes whenever the members change. */
    public int revision() {
        return revision;
    }

    /** Returns whether this network was merged into another one or lost its last member. */
    public boolean isRemoved() {
        return removed;
    }

    void add(N node) {
        members.add(node);
        revision++;
    }

    void addAll(Collection<N> nodes) {
        members.addAll(nodes);
        revision++;
    }

    void remove(N node) {
        members.remove(node);
        revision++;
    }

    void removeAll(Collection<N> nodes) {
        members.removeAll(nodes);
        revision++;
    }

    void markRemoved() {
        members.clear();
        removed = true;
        revision++;
    }
}
