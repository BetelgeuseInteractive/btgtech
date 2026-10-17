package com.betelgeuseinteractive.btgtech.core.machine;

import net.minecraft.core.Direction;

/**
 * Which faces of a machine-like block are open. Closed faces expose no capabilities, so cables and pipes neither
 * connect nor render there; the front starts closed (ADR 0035).
 */
public final class FaceStates {
    private int closedMask;

    private FaceStates(int closedMask) {
        this.closedMask = closedMask;
    }

    /** Returns the default states for a block facing the given direction: the front closed, all other faces open. */
    public static FaceStates withClosedFront(Direction front) {
        return new FaceStates(bit(front));
    }

    /** Returns whether the face on the given side is open. */
    public boolean isOpen(Direction side) {
        return (closedMask & bit(side)) == 0;
    }

    /** Opens a closed face or closes an open one. */
    public void toggle(Direction side) {
        closedMask ^= bit(side);
    }

    int closedMask() {
        return closedMask;
    }

    void restore(int closedMask) {
        this.closedMask = closedMask & 0b111111;
    }

    private static int bit(Direction side) {
        return 1 << side.get3DDataValue();
    }
}
