# 0053. Tags, world generation, creative tab and advancements

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Mining rules, ore placement, creative-mode organization and in-game guidance must be defined. v1 has no guidebook (ADR 0006).

## Options Considered
The rules below were proposed as one set and approved, including a new content item: a small set of guiding advancements.

## Decision
- **Material tags:** every material form is in its `c:` tag (ADR 0038).
- **Mining:** blocks are in `minecraft:mineable/pickaxe`. Required tool: stone pickaxe for tin ore and all machine-like blocks; iron pickaxe for lead ore.
- **World generation:** ores are added to `#minecraft:is_overworld` biomes through a NeoForge biome modifier; parameters come from the balance table.
- **Creative tab:** one "BTG Tech" tab; content grouped by feature, each group ordered by tier.
- **Advancements:** 8–10 progression advancements act as a lightweight guide (e.g. first tin → first generator → first Advanced machine → … → Ultimate). They are produced by data generation. This extends the v1 content list of ADR 0006.

## Consequences
- An in-game guidebook remains out of v1 scope.
