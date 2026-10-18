package com.betelgeuseinteractive.btgtech.energy;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.core.Tier;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Items of the energy feature: the block items, one per tier. */
public final class EnergyItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BTGTech.MOD_ID);

    /** The battery items by tier. */
    public static final Map<Tier, DeferredItem<BatteryItem>> BATTERIES = registerBatteries();

    private EnergyItems() {}

    /** Adds the items to the item registry. */
    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    /** Returns the items in creative tab order: each block type ordered by tier. */
    static List<DeferredItem<? extends Item>> creativeTabOrder() {
        return List.copyOf(BATTERIES.values());
    }

    private static Map<Tier, DeferredItem<BatteryItem>> registerBatteries() {
        Map<Tier, DeferredItem<BatteryItem>> batteries = new EnumMap<>(Tier.class);
        for (Tier tier : Tier.values()) {
            batteries.put(
                    tier,
                    ITEMS.registerItem(
                            tier.id() + "_battery",
                            properties -> new BatteryItem(
                                    EnergyBlocks.BATTERIES.get(tier).get(), properties),
                            properties -> properties.useBlockDescriptionPrefix()));
        }
        return Collections.unmodifiableMap(batteries);
    }
}
