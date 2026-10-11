# Phase 3: Machines → `0.3.0`

Goal: complete the v1 processing machines and materials.

## WP-301 Materials part 2

- **Scope:** lead (all forms, world generation), steel (all forms); dust and plate for iron, copper and gold; casings and circuits for Advanced, Elite and Ultimate; tier upgrade recipes for all existing tiered blocks.
- **Acceptance:** lead generates per balance table; data checks pass.
- **Depends on:** WP-108.
- **References:** ADR 0033, 0038, 0040.

## WP-302 Electric furnace

- **Scope:** `electric_furnace` in four tiers processing `minecraft:smelting`.
- **Acceptance:** game test: smelts a vanilla and a modded smelting recipe.
- **Depends on:** WP-108.
- **References:** ADR 0036, 0039.

## WP-303 Compressor

- **Scope:** `compressor` in four tiers; `btgtech:compressing`; ingot → plate recipes.
- **Depends on:** WP-301.
- **References:** ADR 0036, 0039.

## WP-304 Alloy smelter

- **Scope:** `alloy_smelter` in four tiers; `btgtech:alloying`; bronze and steel recipes (steel with minimum tier if balance requires).
- **Depends on:** WP-301.
- **References:** ADR 0036, 0038, 0039.

## WP-305 Fluid processor

- **Scope:** `fluid_processor` in four tiers; GUI mode switch (fill / drain) through the vanilla menu button mechanism; `btgtech:fluid_filling` and `btgtech:fluid_draining`; `0.3.0` release.
- **Acceptance:** game tests for both modes.
- **Depends on:** WP-201.
- **References:** ADR 0036, 0039, 0055.
