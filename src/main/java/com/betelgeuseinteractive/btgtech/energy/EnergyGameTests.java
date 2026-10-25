package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.BalanceConfig;
import com.betelgeuseinteractive.btgtech.core.Tier;
import com.betelgeuseinteractive.btgtech.core.machine.MachineBlock;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/**
 * Game tests of the energy feature (testing.md, section 4). Each test places its blocks on the empty platform; the test
 * instances are produced by data generation.
 */
public final class EnergyGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> TEST_FUNCTIONS =
            DeferredRegister.create(Registries.TEST_FUNCTION, BTGTech.MOD_ID);
    private static final BlockPos BATTERY = new BlockPos(1, 1, 1);
    private static final BlockPos GENERATOR = new BlockPos(1, 1, 1);
    private static final int BURN_TEST_TICKS = 40;

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BATTERY_FILLS_UP =
            TEST_FUNCTIONS.register("battery_fills_up_to_its_capacity", () -> EnergyGameTests::batteryFillsUp);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BATTERY_COMPARATOR =
            TEST_FUNCTIONS.register(
                    "battery_comparator_follows_fill_level", () -> EnergyGameTests::batteryComparatorFollowsFill);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BATTERY_REDSTONE =
            TEST_FUNCTIONS.register(
                    "battery_transfers_only_while_redstone_allows", () -> EnergyGameTests::batteryFollowsRedstone);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GENERATOR_OUTPUT =
            TEST_FUNCTIONS.register(
                    "generator_burns_coal_at_its_tier_rate", () -> EnergyGameTests::generatorBurnsAtTierRate);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GENERATOR_FUEL_ENERGY =
            TEST_FUNCTIONS.register(
                    "generator_fuel_energy_is_the_same_on_every_tier", () -> EnergyGameTests::generatorFuelEnergy);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GENERATOR_REFUSES =
            TEST_FUNCTIONS.register("generator_refuses_non_fuel", () -> EnergyGameTests::generatorRefusesNonFuel);

    /** All test functions of the feature, for data generation. */
    public static final List<DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>>> ALL = List.of(
            BATTERY_FILLS_UP,
            BATTERY_COMPARATOR,
            BATTERY_REDSTONE,
            GENERATOR_OUTPUT,
            GENERATOR_FUEL_ENERGY,
            GENERATOR_REFUSES);

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

    // A Basic generator fed with coal produces its tier's output each tick, within one tick of the start.
    private static void generatorBurnsAtTierRate(GameTestHelper helper) {
        placeGenerator(helper, Tier.BASIC);
        helper.assertValueEqual(insertItem(helper, Items.COAL, 1), 1, "coal taken by the fuel slot");
        int output = BalanceConfig.SOLID_FUEL_GENERATOR_OUTPUT.get(Tier.BASIC);

        helper.runAfterDelay(BURN_TEST_TICKS, () -> {
            long stored = generatorEnergy(helper).getAmountAsLong();
            helper.assertTrue(
                    Math.abs(stored - (long) BURN_TEST_TICKS * output) <= output,
                    "a Basic generator stored " + stored + " FE after " + BURN_TEST_TICKS + " ticks");
            helper.assertTrue(
                    helper.getLevel()
                            .getBlockState(helper.absolutePos(GENERATOR))
                            .getValue(MachineBlock.ACTIVE),
                    "a burning generator shows its active front");
            helper.succeed();
        });
    }

    // An Ultimate generator burns one coal far faster but releases exactly the energy a Basic one gets from it.
    private static void generatorFuelEnergy(GameTestHelper helper) {
        placeGenerator(helper, Tier.ULTIMATE);
        insertItem(helper, Items.COAL, 1);
        long coalEnergy = (long) Items.COAL
                        .getDefaultInstance()
                        .getBurnTime(RecipeType.SMELTING, helper.getLevel().fuelValues())
                * BalanceConfig.SOLID_FUEL_ENERGY_PER_BURN_TICK.getAsInt();

        helper.runAfterDelay(BURN_TEST_TICKS, () -> {
            helper.assertValueEqual(generatorEnergy(helper).getAmountAsLong(), coalEnergy, "energy from one coal");
            helper.succeed();
        });
    }

    // The fuel slot takes fuel only, from other blocks as well as from players.
    private static void generatorRefusesNonFuel(GameTestHelper helper) {
        placeGenerator(helper, Tier.BASIC);

        helper.assertValueEqual(insertItem(helper, Items.DIRT, 1), 0, "dirt taken by the fuel slot");
        helper.assertValueEqual(insertItem(helper, Items.COAL, 1), 1, "coal taken by the fuel slot");
        helper.succeed();
    }

    private static void placeGenerator(GameTestHelper helper, Tier tier) {
        helper.setBlock(GENERATOR, EnergyBlocks.SOLID_FUEL_GENERATORS.get(tier).get());
    }

    private static EnergyHandler generatorEnergy(GameTestHelper helper) {
        EnergyHandler energy =
                helper.getLevel().getCapability(Capabilities.Energy.BLOCK, helper.absolutePos(GENERATOR), Direction.UP);
        helper.assertTrue(energy != null, "The generator exposes an energy handler on its top face");
        return energy;
    }

    private static int insertItem(GameTestHelper helper, Item item, int count) {
        ResourceHandler<ItemResource> slots =
                helper.getLevel().getCapability(Capabilities.Item.BLOCK, helper.absolutePos(GENERATOR), Direction.UP);
        helper.assertTrue(slots != null, "The generator exposes an item handler on its top face");
        try (Transaction transaction = Transaction.openRoot()) {
            int inserted = ResourceHandlerUtil.insertStacking(slots, ItemResource.of(item), count, transaction);
            transaction.commit();
            return inserted;
        }
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
