# Phase 2: Energy → `0.2.0`

Goal: complete the v1 energy content.

## WP-201 Fluid tank component

- **Goal:** machines can hold fluids.
- **Scope:** fluid tank component in `core.machine` using `ResourceHandler<FluidResource>`; fluid gauge widget; clientbound payload for fluid type in open menus (ADR 0055).
- **Acceptance:** unit/game tests for fill and drain through the capability.
- **Depends on:** WP-108.
- **References:** ADR 0029, 0034, 0044, 0054, 0055.

## WP-202 Solar panel

- **Scope:** `solar_panel` in four tiers (Blockbench model); daytime and sky-access output, rain 50 %, night 0; output computed in one method (space extension point, ADR 0071); no dimension checks (ADR 0070).
- **Acceptance:** game tests for day, night and covered panel.
- **Depends on:** WP-108.
- **References:** ADR 0039, 0043, 0070, 0071.

## WP-203 Geothermal generator

- **Scope:** `geothermal_generator` in four tiers with an internal tank; data map `btgtech:geothermal_fuels` (default lava, 200,000 FE per 1,000 mB); bucket and pipe filling.
- **Acceptance:** game test: lava produces the configured energy.
- **Depends on:** WP-201.
- **References:** ADR 0039, 0043.

## WP-204 Generator push and balance config

- **Scope:** generators push energy to adjacent acceptors except BTG cables (ADR 0041); all Phase 2 balance values in the `SERVER` config; `0.2.0` release.
- **Acceptance:** game tests: generator powers an adjacent machine without a cable; no double transfer into cables.
- **Depends on:** WP-202, WP-203.
- **References:** ADR 0025, 0041, 0043.
