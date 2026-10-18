package com.betelgeuseinteractive.btgtech.core;

import com.betelgeuseinteractive.btgtech.BTGTech;
import java.util.List;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Items shared by several features: the tiered crafting components (ADR 0040). */
public final class CoreItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BTGTech.MOD_ID);

    public static final DeferredItem<BlockItem> BASIC_MACHINE_CASING =
            ITEMS.registerSimpleBlockItem(CoreBlocks.BASIC_MACHINE_CASING);
    public static final DeferredItem<Item> BASIC_CIRCUIT = ITEMS.registerSimpleItem("basic_circuit");

    /** The components in creative tab order, by tier. */
    static final List<DeferredItem<? extends Item>> CREATIVE_TAB_ORDER = List.of(BASIC_MACHINE_CASING, BASIC_CIRCUIT);

    private CoreItems() {}

    /** Adds the items to the item registry. */
    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}
