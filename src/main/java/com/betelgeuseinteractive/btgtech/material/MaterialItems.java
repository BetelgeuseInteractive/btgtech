package com.betelgeuseinteractive.btgtech.material;

import com.betelgeuseinteractive.btgtech.BTGTech;
import java.util.List;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Items of the materials (ADR 0038): the item forms and the items of the material blocks. */
public final class MaterialItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BTGTech.MOD_ID);

    public static final DeferredItem<BlockItem> TIN_ORE = ITEMS.registerSimpleBlockItem(MaterialBlocks.TIN_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_TIN_ORE =
            ITEMS.registerSimpleBlockItem(MaterialBlocks.DEEPSLATE_TIN_ORE);
    public static final DeferredItem<Item> RAW_TIN = ITEMS.registerSimpleItem("raw_tin");
    public static final DeferredItem<Item> TIN_INGOT = ITEMS.registerSimpleItem("tin_ingot");
    public static final DeferredItem<Item> TIN_NUGGET = ITEMS.registerSimpleItem("tin_nugget");
    public static final DeferredItem<Item> TIN_DUST = ITEMS.registerSimpleItem("tin_dust");
    public static final DeferredItem<Item> TIN_PLATE = ITEMS.registerSimpleItem("tin_plate");
    public static final DeferredItem<BlockItem> TIN_BLOCK = ITEMS.registerSimpleBlockItem(MaterialBlocks.TIN_BLOCK);
    public static final DeferredItem<BlockItem> RAW_TIN_BLOCK =
            ITEMS.registerSimpleBlockItem(MaterialBlocks.RAW_TIN_BLOCK);

    public static final DeferredItem<Item> BRONZE_INGOT = ITEMS.registerSimpleItem("bronze_ingot");
    public static final DeferredItem<Item> BRONZE_NUGGET = ITEMS.registerSimpleItem("bronze_nugget");
    public static final DeferredItem<Item> BRONZE_DUST = ITEMS.registerSimpleItem("bronze_dust");
    public static final DeferredItem<Item> BRONZE_PLATE = ITEMS.registerSimpleItem("bronze_plate");
    public static final DeferredItem<BlockItem> BRONZE_BLOCK =
            ITEMS.registerSimpleBlockItem(MaterialBlocks.BRONZE_BLOCK);

    /** All material items in creative tab order: tin, then bronze, each from ore to storage block. */
    static final List<DeferredItem<? extends Item>> CREATIVE_TAB_ORDER = List.of(
            TIN_ORE,
            DEEPSLATE_TIN_ORE,
            RAW_TIN,
            TIN_INGOT,
            TIN_NUGGET,
            TIN_DUST,
            TIN_PLATE,
            TIN_BLOCK,
            RAW_TIN_BLOCK,
            BRONZE_INGOT,
            BRONZE_NUGGET,
            BRONZE_DUST,
            BRONZE_PLATE,
            BRONZE_BLOCK);

    private MaterialItems() {}

    /** Adds the items to the item registry. */
    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}
