package com.betelgeuseinteractive.btgtech.data;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Every block has a block state, the models it uses, an item definition, a loot table and a translation, and every item
 * has a translation (testing.md, section 3). Blocks are found through their block states and their translation keys,
 * so a block missing either is reported.
 */
class BlockResourcesTest {
    private static final String BLOCK_STATES = "assets/btgtech/blockstates";
    private static final String ITEMS = "assets/btgtech/items";
    private static final String BLOCK_KEY = "block.btgtech.";
    private static final String ITEM_KEY = "item.btgtech.";

    @Test
    void everyBlockHasItsResources() {
        Set<String> keys = englishKeys();
        List<String> problems = new ArrayList<>();
        for (String block : ModResources.jsonNames(BLOCK_STATES)) {
            for (String model : models(ModResources.json(BLOCK_STATES + "/" + block + ".json"))) {
                requireResource(problems, block, modelPath(model));
            }
            requireResource(problems, block, ITEMS + "/" + block + ".json");
            requireResource(problems, block, "data/btgtech/loot_table/blocks/" + block + ".json");
            if (!keys.contains(BLOCK_KEY + block)) {
                problems.add(block + ": no translation");
            }
        }

        assertTrue(problems.isEmpty(), () -> String.join("\n", problems));
    }

    @Test
    void everyBlockTranslationBelongsToABlockState() {
        List<String> blocks = ModResources.jsonNames(BLOCK_STATES);
        List<String> orphans = englishKeys().stream()
                .filter(key -> key.startsWith(BLOCK_KEY))
                .map(key -> key.substring(BLOCK_KEY.length()))
                .filter(block -> !blocks.contains(block))
                .toList();

        assertTrue(orphans.isEmpty(), () -> "Translations without a block state: " + orphans);
    }

    @Test
    void everyItemHasATranslation() {
        Set<String> keys = englishKeys();
        List<String> untranslated = ModResources.jsonNames(ITEMS).stream()
                .filter(item -> !keys.contains(ITEM_KEY + item) && !keys.contains(BLOCK_KEY + item))
                .toList();

        assertTrue(untranslated.isEmpty(), () -> "Items without a translation: " + untranslated);
    }

    private static void requireResource(List<String> problems, String block, String path) {
        if (!ModResources.exists(path)) {
            problems.add(block + ": missing " + path);
        }
    }

    private static Set<String> englishKeys() {
        return ModResources.json("assets/btgtech/lang/en_us.json")
                .getAsJsonObject()
                .keySet();
    }

    // The models of a block state with variants; multipart block states are not used yet.
    private static List<String> models(JsonElement blockState) {
        List<String> models = new ArrayList<>();
        JsonObject variants = blockState.getAsJsonObject().getAsJsonObject("variants");
        for (String variant : variants.keySet()) {
            JsonElement entry = variants.get(variant);
            List<JsonElement> options = new ArrayList<>();
            if (entry.isJsonArray()) {
                entry.getAsJsonArray().forEach(options::add);
            } else {
                options.add(entry);
            }
            options.forEach(
                    option -> models.add(option.getAsJsonObject().get("model").getAsString()));
        }
        return models;
    }

    private static String modelPath(String model) {
        int separator = model.indexOf(':');
        return "assets/" + model.substring(0, separator) + "/models/" + model.substring(separator + 1) + ".json";
    }
}
