package com.betelgeuseinteractive.btgtech.energy.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FuelBurnerTest {
    private static final int COAL = 64_000;

    private final FuelBurner burner = new FuelBurner();

    @Test
    void startsWithoutFuel() {
        assertFalse(burner.isBurning());
        assertEquals(0, burner.burn(40, 10_000));
    }

    @Test
    void eachTickReleasesAtMostTheOutput() {
        burner.start(COAL);

        assertEquals(40, burner.burn(40, 10_000));
        assertEquals(COAL - 40, burner.remaining());
        assertEquals(COAL, burner.total());
    }

    @Test
    void theSameFuelReleasesTheSameEnergyAtEveryOutput() {
        assertEquals(COAL, burnOut(40));
        assertEquals(COAL, burnOut(2_560));
        assertEquals(COAL, burnOut(3_000));
    }

    @Test
    void higherOutputsBurnFaster() {
        assertEquals(1_600, ticksToBurnOut(40));
        assertEquals(25, ticksToBurnOut(2_560));
    }

    @Test
    void nothingBurnsWithoutRoomAndPartialRoomLimitsTheRelease() {
        burner.start(COAL);

        assertEquals(0, burner.burn(40, 0));
        assertEquals(COAL, burner.remaining());
        assertEquals(15, burner.burn(40, 15));
    }

    @Test
    void theLastTickReleasesOnlyWhatIsLeftAndEndsTheFuel() {
        burner.start(100);

        assertEquals(40, burner.burn(40, 1_000));
        assertEquals(40, burner.burn(40, 1_000));
        assertEquals(20, burner.burn(40, 1_000));
        assertFalse(burner.isBurning());
        assertEquals(0, burner.total());
    }

    @Test
    void startRejectsEmptyFuelAndFuelWhileBurning() {
        assertThrows(IllegalArgumentException.class, () -> burner.start(0));
        burner.start(100);
        assertThrows(IllegalStateException.class, () -> burner.start(100));
    }

    @Test
    void restoreClampsSavedValues() {
        burner.restore(500, 200);
        assertEquals(200, burner.remaining());

        burner.restore(-3, 200);
        assertFalse(burner.isBurning());
        assertEquals(0, burner.total());
    }

    private int burnOut(int output) {
        FuelBurner fuel = new FuelBurner();
        fuel.start(COAL);
        int released = 0;
        while (fuel.isBurning()) {
            released += fuel.burn(output, Integer.MAX_VALUE);
        }
        assertTrue(released > 0);
        return released;
    }

    private int ticksToBurnOut(int output) {
        FuelBurner fuel = new FuelBurner();
        fuel.start(COAL);
        int ticks = 0;
        while (fuel.isBurning()) {
            fuel.burn(output, Integer.MAX_VALUE);
            ticks++;
        }
        return ticks;
    }
}
