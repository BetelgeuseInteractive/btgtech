package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.BalanceConfig;
import com.betelgeuseinteractive.btgtech.core.Tier;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/**
 * Game tests of the energy feature (testing.md, section 4). Each test places its blocks on the empty platform; the test
 * instances are produced by data generation.
 */
public final class EnergyGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> TEST_FUNCTIONS =
            DeferredRegister.create(Registries.TEST_FUNCTION, BTGTech.MOD_ID);
    private static final BlockPos BATTERY = new BlockPos(1, 1, 1);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BATTERY_FILLS_UP =
            TEST_FUNCTIONS.register("battery_fills_up_to_its_capacity", () -> EnergyGameTests::batteryFillsUp);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BATTERY_COMPARATOR =
            TEST_FUNCTIONS.register(
                    "battery_comparator_follows_fill_level", () -> EnergyGameTests::batteryComparatorFollowsFill);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BATTERY_REDSTONE =
            TEST_FUNCTIONS.register(
                    "battery_transfers_only_while_redstone_allows", () -> EnergyGameTests::batteryFollowsRedstone);

    /** All test functions of the feature, for data generation. */
    public static final List<DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>>> ALL =
            List.of(BATTERY_FILLS_UP, BATTERY_COMPARATOR, BATTERY_REDSTONE);

    private EnergyGameTests() {}

    /** Adds the test functions to their registry. */
    static void register(IEventBus modBus) {
        TEST_FUNCTIONS.register(modBus);
    }

    // A battery takes at most its transfer rate per insertion, fills up to its capacity and then refuses more.
    private static void batteryFillsUp(GameTestHelper helper) {
        EnergyHandler energy = placeBattery(helper);
        int capacity = BalanceConfig.BATTERY_CAPACITY.get(Tier.BASIC);
        int transfer = BalanceConfig.BATTERY_TRANSFER.get(Tier.BASIC);

        helper.assertValueEqual(insert(energy, transfer * 5), transfer, "energy taken by one insertion");
        fill(energy, capacity);
        helper.assertValueEqual(energy.getAmountAsLong(), (long) capacity, "stored energy");
        helper.assertValueEqual(insert(energy, transfer), 0, "energy taken by a full battery");
        helper.succeed();
    }

    // The comparator output is 0 when empty, 8 at half capacity and 15 when full.
    private static void batteryComparatorFollowsFill(GameTestHelper helper) {
        EnergyHandler energy = placeBattery(helper);
        int capacity = BalanceConfig.BATTERY_CAPACITY.get(Tier.BASIC);

        helper.assertValueEqual(comparator(helper), 0, "comparator output when empty");
        fill(energy, capacity / 2);
        helper.assertValueEqual(comparator(helper), 8, "comparator output at half capacity");
        fill(energy, capacity);
        helper.assertValueEqual(comparator(helper), 15, "comparator output when full");
        helper.succeed();
    }

    // In "active with signal" mode, the battery refuses energy until it receives a redstone signal.
    private static void batteryFollowsRedstone(GameTestHelper helper) {
        EnergyHandler energy = placeBattery(helper);
        helper.getBlockEntity(BATTERY, BatteryBlockEntity.class).cycleRedstoneMode();

        helper.assertValueEqual(insert(energy, 100), 0, "energy taken without a signal");
        helper.setBlock(BATTERY.east(), Blocks.REDSTONE_BLOCK);
        helper.assertValueEqual(insert(energy, 100), 100, "energy taken with a signal");
        helper.succeed();
    }

    private static EnergyHandler placeBattery(GameTestHelper helper) {
        helper.setBlock(BATTERY, EnergyBlocks.BATTERIES.get(Tier.BASIC).get());
        EnergyHandler energy =
                helper.getLevel().getCapability(Capabilities.Energy.BLOCK, helper.absolutePos(BATTERY), Direction.UP);
        helper.assertTrue(energy != null, "The battery exposes an energy handler on its top face");
        return energy;
    }

    private static void fill(EnergyHandler energy, long target) {
        while (energy.getAmountAsLong() < target) {
            int inserted = insert(energy, (int) Math.min(Integer.MAX_VALUE, target - energy.getAmountAsLong()));
            if (inserted == 0) {
                return;
            }
        }
    }

    private static int insert(EnergyHandler energy, int amount) {
        try (Transaction transaction = Transaction.openRoot()) {
            int inserted = energy.insert(amount, transaction);
            transaction.commit();
            return inserted;
        }
    }

    private static int comparator(GameTestHelper helper) {
        BlockPos pos = helper.absolutePos(BATTERY);
        BlockState state = helper.getLevel().getBlockState(pos);
        return state.getAnalogOutputSignal(helper.getLevel(), pos, Direction.NORTH);
    }
}
