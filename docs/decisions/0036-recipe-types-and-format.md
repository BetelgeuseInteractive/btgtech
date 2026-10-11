# 0036. Recipe types and format

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Recipes define progression, must be customizable by modpack makers and must work with recipe viewers. NeoForge 26.1 custom recipes consist of `Recipe`, `RecipeInput`, `RecipeType` and a `RecipeSerializer` (`MapCodec` + `StreamCodec`); recipes are generated with `RecipeProvider`. In 26.3 recipes become reloadable datapack registries and datagen providers change, so recipe code is port-sensitive.

## Options Considered
- **A. Own datapack recipe types only.**
- **B. Recipes hard-coded in Java:** not customizable.
- **C. Own types plus vanilla types where they fit.**

## Decision
Option C:
- The electric furnace processes vanilla `minecraft:smelting` recipes, so every mod's smelting recipes work automatically.
- Other processing machines use one BTG recipe type each: `btgtech:crushing`, `btgtech:compressing`, `btgtech:alloying`, `btgtech:fluid_filling`, `btgtech:fluid_draining`.
- Shared recipe base classes and codecs live in `core.machine`; type-specific code is minimal.
- Recipes are data (JSON in datapacks), produced by data generation; none are hard-coded.

Recipe fields:

| Field | Rule |
|---|---|
| Inputs | `SizedIngredient` / `SizedFluidIngredient`; common `c:` tags wherever possible (e.g. `c:ingots/tin`) |
| Outputs | One or more results; optional **chance-based byproducts** (e.g. 25 % extra dust) |
| Processing time | Optional base duration in ticks; scaled by machine tier and `SERVER` config. Energy = duration × the machine's per-tick usage |
| Minimum tier | Optional; default `basic`. A machine below the minimum tier does not run the recipe |

## Consequences
- Recipe classes are concentrated in `core.machine` so 26.3+ ports change one place.
- Out of v1 scope: recipe chaining / auto-crafting in machines, and complex data-component conditions beyond NeoForge's standard recipe conditions.
