package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.Tier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Items of the energy feature: the block items, one per tier. */
public final class EnergyItems {
    /** Translation key of the battery tooltip line: a broken battery loses its stored energy (ADR 0052). */
    public static final String BATTERY_LOSES_ENERGY = "tooltip.btgtech.battery_loses_energy";

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BTGTech.MOD_ID);

    /** The solid fuel generator items by tier. */
    public static final Map<Tier, DeferredItem<BlockItem>> SOLID_FUEL_GENERATORS = registerGenerators();
    /** The battery items by tier. */
    public static final Map<Tier, DeferredItem<BlockItem>> BATTERIES = registerBatteries();

    private EnergyItems() {}

    /** Adds the items to the item registry. */
    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    /** Returns the items in creative tab order: each block type ordered by tier. */
    static List<DeferredItem<? extends Item>> creativeTabOrder() {
        List<DeferredItem<? extends Item>> items = new ArrayList<>(SOLID_FUEL_GENERATORS.values());
        items.addAll(BATTERIES.values());
        return List.copyOf(items);
    }

    private static Map<Tier, DeferredItem<BlockItem>> registerGenerators() {
        Map<Tier, DeferredItem<BlockItem>> generators = new EnumMap<>(Tier.class);
        for (Tier tier : Tier.values()) {
            generators.put(tier, ITEMS.registerSimpleBlockItem(EnergyBlocks.SOLID_FUEL_GENERATORS.get(tier)));
        }
        return Collections.unmodifiableMap(generators);
    }

    private static Map<Tier, DeferredItem<BlockItem>> registerBatteries() {
        // The warning is a default lore line, styled like other tooltip details instead of lore.
        ItemLore warning = new ItemLore(List.of(Component.translatable(BATTERY_LOSES_ENERGY)
                .withStyle(style -> style.withColor(ChatFormatting.GRAY).withItalic(false))));
        Map<Tier, DeferredItem<BlockItem>> batteries = new EnumMap<>(Tier.class);
        for (Tier tier : Tier.values()) {
            batteries.put(
                    tier,
                    ITEMS.registerSimpleBlockItem(
                            EnergyBlocks.BATTERIES.get(tier),
                            properties -> properties.component(DataComponents.LORE, warning)));
        }
        return Collections.unmodifiableMap(batteries);
    }
}
