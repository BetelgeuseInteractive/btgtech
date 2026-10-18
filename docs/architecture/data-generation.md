# Data Generation and Content Data

How BTG Tech produces models, loot, recipes, tags, world generation, advancements and translations. Decisions: ADR [0050](../decisions/0050-data-generation-scope-and-output-policy.md)–[0053](../decisions/0053-tags-world-generation-creative-tab-and-advancements.md), [0048](../decisions/0048-generated-tier-texture-variants.md).

## 1. Pipeline

- Run: `runData` (ADR 0015). All providers are registered on `GatherDataEvent.Client` via `event.createProvider(...)` in `DataGenerators`, which `BTGTechClient` registers.
- Output: `src/generated/resources/`, committed; CI fails if regenerated output differs.
- Code: `datagen` package, one class per provider type, organized in per-feature sections. `datagen` may use client classes such as the model provider, and only client code references it (ADR [0075](../decisions/0075-data-generation-may-use-client-classes.md)).

| Provider | Produces |
|---|---|
| Model provider | Block states, block and item models, client item definitions |
| Language provider | `en_us.json` |
| Loot table provider | Block loot tables (ADR 0052) |
| Recipe provider | Crafting, smelting and all BTG recipe types |
| Tag providers | Block and item tags (`c:` and `minecraft:` tags) |
| Data map provider | `btgtech:geothermal_fuels` |
| Datapack built-in entries provider | Configured features, placed features, biome modifiers |
| Advancement provider | Progression advancements |
| Tier texture provider (BTG) | Tier texture variants from base texture + tier mask (ADR 0048); `TierTextureProvider` lists the base textures, starting with the machine screen panel |
| Sprite source provider | Atlas sources that recolor vanilla textures at runtime for material forms (ADR 0074) |

Writing PNG files from data generation was proven in WP-004 (ADR 0048): a provider reads the base texture and the tier mask from the existing resources (`event.getResourceManager(PackType.CLIENT_RESOURCES)` includes `src/main/resources`), combines them with `NativeImage` and `ARGB`, and passes the encoded bytes to `CachedOutput.writeIfNeeded`. `NativeImage` can only write to a file, so the bytes are read back from a temporary file. The output is byte-identical between runs and between Windows and Linux (CI), so the freshness check holds. Under the mask, every pixel must be one of the three Basic tier tones and every mask pixel pure black or white; anything else fails data generation.

## 2. Hand-Made Resources

Only: base textures and tier masks, `.mcmeta` animations, Blockbench model exports, palette strips for runtime recoloring (ADR 0074), and translation files other than `en_us` (in `src/main/resources/assets/btgtech/lang/`).

## 3. Translations

- Languages: `en_us` (default), `tr_tr`, `zh_cn`, `ru_ru`, `pt_br`, `es_es`, `de_de`, `fr_fr`.
- Keys: `block.btgtech.<id>`, `item.btgtech.<id>`, `gui.btgtech.<name>`, `tooltip.btgtech.<name>`, `itemGroup.btgtech.<name>`, `advancements.btgtech.<name>.title|description`.
- Every block and item name is a full string per language; no composition in code.
- A test checks that every language contains every `en_us` key.

## 4. Loot

See ADR 0052: ores drop raw ore (Fortune / Silk Touch as vanilla); machine-like blocks drop themselves and spill their inventory, resetting energy and settings; storage blocks keep contents; cables, pipes and material blocks drop themselves.

## 5. Tags and Mining

- Materials in `c:` tags (`c:ores/tin`, `c:ingots/bronze`, `c:dusts/lead`, `c:plates/steel`, …).
- All BTG blocks in `minecraft:mineable/pickaxe`; stone tool for tin ore and machine-like blocks, iron tool for lead ore.

## 6. World Generation

Configured and placed features for tin and lead ore, added to `#minecraft:is_overworld` with a NeoForge biome modifier. Parameters: [balance.md §5](balance.md#5-ore-generation-overworld).

## 7. Creative Tab and Advancements

- One "BTG Tech" creative tab (`btgtech:btgtech`, title key `itemGroup.btgtech.btgtech`), defined in `core`. Each feature adds its items with a `BuildCreativeModeTabContentsEvent` listener in its bootstrap class, ordered by tier; the bootstrap order therefore groups the tab by feature. The tab is hidden while it has no items.
- 8–10 progression advancements guide the player from the first tin ingot to Ultimate tier.
