package com.betelgeuseinteractive.btgtech.datagen;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.energy.EnergyGameTests;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestEnvironments;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Game test instances (testing.md, section 4): one per test function of the features, all on the empty platform in the
 * default environment. The test functions live in the features they test.
 */
final class GameTestEntries {
    /** The empty platform every test places its blocks on (ADR 0060). */
    static final Identifier PLATFORM = Identifier.fromNamespaceAndPath(BTGTech.MOD_ID, "game_test_platform");

    private static final int MAX_TICKS = 100;

    private GameTestEntries() {}

    /** Adds the test instances to the datapack entries of data generation. */
    static void addTo(RegistrySetBuilder builder) {
        builder.add(Registries.TEST_INSTANCE, GameTestEntries::testInstances);
    }

    private static void testInstances(BootstrapContext<GameTestInstance> context) {
        Holder<TestEnvironmentDefinition<?>> environment =
                context.lookup(Registries.TEST_ENVIRONMENT).getOrThrow(GameTestEnvironments.DEFAULT_KEY);
        List<DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>>> functions = EnergyGameTests.ALL;
        for (DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> function : functions) {
            ResourceKey<Consumer<GameTestHelper>> key = function.getKey();
            context.register(
                    ResourceKey.create(Registries.TEST_INSTANCE, key.identifier()),
                    new FunctionGameTestInstance(key, new TestData<>(environment, PLATFORM, MAX_TICKS, 0, true)));
        }
    }
}
