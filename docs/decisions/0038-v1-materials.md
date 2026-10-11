# 0038. v1 materials

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
ADR 0008 approved a small custom material set (2–3 ores, 2 alloys) and left the exact materials open. Four tiers need distinct progression materials. NeoForge defines common tags such as `c:ores`, `c:ingots`, `c:dusts`, `c:nuggets`, `c:raw_materials`, `c:storage_blocks`; `c:plates` is not defined by NeoForge but is a de-facto convention among mods.

## Options Considered
Two versus three new ores. Elite and Ultimate tiers can be gated by vanilla materials (gold, diamond, redstone, netherite, ender pearl), so a third ore has no concrete need.

## Decision

| Material | Kind | Purpose | Forms |
|---|---|---|---|
| Tin (`tin`) | Ore | Bronze; Basic tier | stone ore, deepslate ore, raw, ingot, nugget, dust, plate, storage block, raw storage block |
| Lead (`lead`) | Ore | Batteries; later radiation shielding for space technology | Same as tin |
| Bronze (`bronze`) | Alloy | 3 copper + 1 tin; Basic machine casings | ingot, nugget, dust, plate, storage block |
| Steel (`steel`) | Alloy | Iron + coal dust in the alloy smelter; Advanced tier | Same as bronze |

- Vanilla metals iron, copper and gold additionally get a **dust** and a **plate** form.
- Elite and Ultimate tiers use vanilla materials (gold, diamond, redstone, netherite, ender pearl).
- Every material form is added to the matching `c:` tag (e.g. `c:ingots/tin`, `c:plates/bronze`) and recipes use those tags.
- World generation parameters are defined in the balance table (planning topic 8) and data generation (planning topic 10).

## Consequences
- ADR 0008 is fulfilled by this list; adding a material requires a new ADR.
