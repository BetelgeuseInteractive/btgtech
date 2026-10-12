package com.betelgeuseinteractive.btgtech.core;

import com.betelgeuseinteractive.btgtech.BTGTech;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Creative mode tabs of BTG Tech: the single "BTG Tech" tab (ADR 0053).
 *
 * <p>Features add their items with {@code BuildCreativeModeTabContentsEvent} listeners in their bootstrap classes, so
 * the tab is grouped by feature in bootstrap order, and each feature adds its items ordered by tier. The tab stays
 * hidden until it has items.
 */
public final class CoreCreativeModeTabs {
    /** Translation key of the "BTG Tech" tab title. */
    public static final String BTG_TECH_TITLE = "itemGroup.btgtech.btgtech";

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BTGTech.MOD_ID);

    /** The "BTG Tech" tab, {@code btgtech:btgtech}. */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BTG_TECH = CREATIVE_MODE_TABS.register(
            "btgtech",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable(BTG_TECH_TITLE))
                    .build());

    private CoreCreativeModeTabs() {}

    /** Adds the tabs to the creative mode tab registry. */
    public static void register(IEventBus modBus) {
        CREATIVE_MODE_TABS.register(modBus);
    }
}
