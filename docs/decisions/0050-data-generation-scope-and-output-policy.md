# 0050. Data generation scope and output policy

- **Status:** Accepted (amended by [0060](0060-test-strategy.md), [0074](0074-art-style.md))
- **Date:** 2026-10-11

## Context
About 55 blocks need hundreds of JSON resources. NeoForge 26.1 data generation uses `GatherDataEvent.Client` / `.Server` and `event.createProvider(...)` with providers such as `ModelProvider`, `LanguageProvider`, `LootTableProvider`, `RecipeProvider`, `TagsProvider`, `DataMapProvider`, `DatapackBuiltinEntriesProvider` and `AdvancementProvider`. The official MDK registers all providers on the client side and runs them with one `runData` task.

## Options Considered
For generated output: **A.** commit `src/generated/resources` (MDK default) with a CI freshness check; **B.** regenerate on every build without committing (slower builds).

## Decision
- **Generated:** block states, models and client items; loot tables; recipes; tags; data maps; world generation (configured and placed features, biome modifiers); advancements; the `en_us` translation; tier texture variants (ADR 0048).
- **Hand-made (only these):** base textures and tier masks; `.mcmeta` animations; Blockbench model exports (cables, pipes, solar panel); translation files other than `en_us`.
- Providers live in the `datagen` package (ADR 0018): one class per provider type, organized in per-feature sections. All are registered on `GatherDataEvent.Client` and run by `runData`.
- **Option A:** generated output is committed. CI runs data generation and fails if the committed output differs (planning topic 14).

## Consequences
- Hand-written JSON outside the listed exceptions is not accepted.
- Data generation code is port-sensitive (26.3 changes datagen providers) and is kept in one package.
