package com.betelgeuseinteractive.btgtech.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Data checks of the materials (WP-104): tin ore generation follows the balance table (balance.md, section 5), the loot
 * of ores and storage blocks follows ADR 0052, and every form is in its {@code c:} tag (ADR 0038, 0053).
 */
class MaterialDataTest {
    @Test
    void tinOreVeinsReplaceStoneAndDeepslateWithNineBlocks() {
        JsonObject feature = ModResources.json("data/btgtech/worldgen/configured_feature/ore_tin.json")
                .getAsJsonObject();
        JsonObject config = feature.getAsJsonObject("config");

        assertEquals("minecraft:ore", feature.get("type").getAsString());
        assertEquals(9, config.get("size").getAsInt());
        List<String> targets = new ArrayList<>();
        for (JsonElement target : config.getAsJsonArray("targets")) {
            JsonObject entry = target.getAsJsonObject();
            targets.add(entry.getAsJsonObject("target").get("tag").getAsString() + " -> "
                    + entry.getAsJsonObject("state").get("Name").getAsString());
        }
        assertEquals(
                List.of(
                        "minecraft:stone_ore_replaceables -> btgtech:tin_ore",
                        "minecraft:deepslate_ore_replaceables -> btgtech:deepslate_tin_ore"),
                targets);
    }

    @Test
    void tinOrePlacesTenVeinsPerChunkTriangularFromMinus16To96() {
        JsonArray placement = ModResources.json("data/btgtech/worldgen/placed_feature/ore_tin.json")
                .getAsJsonObject()
                .getAsJsonArray("placement");

        assertEquals("minecraft:count", type(placement.get(0)));
        assertEquals(10, placement.get(0).getAsJsonObject().get("count").getAsInt());
        assertEquals("minecraft:in_square", type(placement.get(1)));
        JsonObject height = placement.get(2).getAsJsonObject().getAsJsonObject("height");
        assertEquals("minecraft:trapezoid", type(height), "a trapezoid without plateau is a triangle");
        assertTrue(!height.has("plateau") || height.get("plateau").getAsInt() == 0);
        assertEquals(
                -16, height.getAsJsonObject("min_inclusive").get("absolute").getAsInt());
        assertEquals(96, height.getAsJsonObject("max_inclusive").get("absolute").getAsInt());
        assertEquals("minecraft:biome", type(placement.get(3)));
    }

    @Test
    void tinOreIsAddedToOverworldBiomes() {
        JsonObject modifier = ModResources.json("data/btgtech/neoforge/biome_modifier/add_ore_tin.json")
                .getAsJsonObject();

        assertEquals("neoforge:add_features", type(modifier));
        assertEquals("#minecraft:is_overworld", modifier.get("biomes").getAsString());
        assertEquals("btgtech:ore_tin", modifier.get("features").getAsString());
        assertEquals("underground_ores", modifier.get("step").getAsString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"tin_ore", "deepslate_tin_ore"})
    void tinOresDropRawTinWithFortuneOrThemselvesWithSilkTouch(String ore) {
        JsonArray choices =
                singleEntry("data/btgtech/loot_table/blocks/" + ore + ".json").getAsJsonArray("children");
        JsonObject silkTouch = choices.get(0).getAsJsonObject();
        JsonObject fortune = choices.get(1).getAsJsonObject();

        assertEquals("btgtech:" + ore, silkTouch.get("name").getAsString());
        assertTrue(silkTouch.get("conditions").toString().contains("minecraft:silk_touch"));
        assertEquals("btgtech:raw_tin", fortune.get("name").getAsString());
        JsonObject bonus = fortune.getAsJsonArray("functions").get(0).getAsJsonObject();
        assertEquals("minecraft:apply_bonus", bonus.get("function").getAsString());
        assertEquals("minecraft:fortune", bonus.get("enchantment").getAsString());
        assertEquals("minecraft:ore_drops", bonus.get("formula").getAsString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"tin_block", "raw_tin_block", "bronze_block", "basic_machine_casing"})
    void otherBlocksDropThemselves(String block) {
        JsonObject entry = singleEntry("data/btgtech/loot_table/blocks/" + block + ".json");

        assertEquals("minecraft:item", type(entry));
        assertEquals("btgtech:" + block, entry.get("name").getAsString());
    }

    @ParameterizedTest
    @CsvSource({
        "item, ores/tin, btgtech:tin_ore btgtech:deepslate_tin_ore",
        "item, raw_materials/tin, btgtech:raw_tin",
        "item, ingots/tin, btgtech:tin_ingot",
        "item, nuggets/tin, btgtech:tin_nugget",
        "item, dusts/tin, btgtech:tin_dust",
        "item, plates/tin, btgtech:tin_plate",
        "item, storage_blocks/tin, btgtech:tin_block",
        "item, storage_blocks/raw_tin, btgtech:raw_tin_block",
        "item, ingots/bronze, btgtech:bronze_ingot",
        "item, nuggets/bronze, btgtech:bronze_nugget",
        "item, dusts/bronze, btgtech:bronze_dust",
        "item, plates/bronze, btgtech:bronze_plate",
        "item, storage_blocks/bronze, btgtech:bronze_block",
        "item, ores, #c:ores/tin",
        "item, ingots, #c:ingots/tin #c:ingots/bronze",
        "item, plates, #c:plates/tin #c:plates/bronze",
        "block, ores/tin, btgtech:tin_ore btgtech:deepslate_tin_ore",
        "block, ores_in_ground/stone, btgtech:tin_ore",
        "block, ores_in_ground/deepslate, btgtech:deepslate_tin_ore",
        "block, storage_blocks, #c:storage_blocks/tin #c:storage_blocks/raw_tin #c:storage_blocks/bronze"
    })
    void formsAreInTheirCommonTags(String registry, String tag, String expected) {
        assertEquals(
                Set.of(expected.split(" ")), tagValues("data/c/tags/" + registry + "/" + tag + ".json"), "c:" + tag);
    }

    @Test
    void materialBlocksAndTheCasingNeedAStonePickaxe() {
        Set<String> blocks = Set.of(
                "btgtech:tin_ore",
                "btgtech:deepslate_tin_ore",
                "btgtech:tin_block",
                "btgtech:raw_tin_block",
                "btgtech:bronze_block",
                "btgtech:basic_machine_casing");

        assertTrue(tagValues("data/minecraft/tags/block/mineable/pickaxe.json").containsAll(blocks));
        assertTrue(tagValues("data/minecraft/tags/block/needs_stone_tool.json").containsAll(blocks));
    }

    private static JsonObject singleEntry(String lootTable) {
        JsonArray pools = ModResources.json(lootTable).getAsJsonObject().getAsJsonArray("pools");
        assertEquals(1, pools.size(), lootTable);
        JsonArray entries = pools.get(0).getAsJsonObject().getAsJsonArray("entries");
        assertEquals(1, entries.size(), lootTable);
        return entries.get(0).getAsJsonObject();
    }

    private static Set<String> tagValues(String tag) {
        Set<String> values = new TreeSet<>();
        ModResources.json(tag)
                .getAsJsonObject()
                .getAsJsonArray("values")
                .forEach(value -> values.add(value.getAsString()));
        return values;
    }

    private static String type(JsonElement element) {
        return element.getAsJsonObject().get("type").getAsString();
    }
}
