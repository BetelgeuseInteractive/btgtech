# 0039. v1 content identifiers and behavior

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
ADR 0006 listed v1 content by working description. Registry identifiers are permanent: changing them later breaks worlds. Tiered blocks get the tier prefix from ADR 0033 (e.g. `basic_crusher`).

## Options Considered
The identifiers and behaviors below were proposed as one set and approved. For the fluid filler/drainer, one block with a mode switch was chosen over two separate blocks.

## Decision

| ID | Feature | Behavior | Slots |
|---|---|---|---|
| `solid_fuel_generator` | energy | Burns items with a burn time in the vanilla `neoforge:furnace_fuels` data map | 1 fuel |
| `solar_panel` | energy | Generates during daytime with sky access; reduced in rain; nothing at night | none |
| `geothermal_generator` | energy | Consumes fluid from its internal tank (filled by pipe or bucket). Accepted fluids and their energy value come from the BTG data map `btgtech:geothermal_fuels` (default: lava) | 1 fluid tank |
| `battery` | energy | Stores energy; comparator output proportional to fill level; passive (does not push) | none |
| `energy_cable` | energy | ADR 0028 | — |
| `electric_furnace` | machine | Vanilla smelting recipes (ADR 0036) | 1 input, 1 output |
| `crusher` | machine | `btgtech:crushing` (e.g. ore → 2 dust + chance byproduct; ingot → dust; cobblestone → gravel → sand) | 1 input, 2 outputs |
| `compressor` | machine | `btgtech:compressing` (e.g. ingot → plate) | 1 input, 1 output |
| `alloy_smelter` | machine | `btgtech:alloying` | 2 inputs, 1 output |
| `fluid_processor` | machine | One block with a GUI mode switch: fill (`btgtech:fluid_filling`) or drain (`btgtech:fluid_draining`) | 1 input, 1 output, 1 tank |
| `item_storage_unit` | storage | ADR 0029 | — |
| `fluid_tank` | storage | ADR 0029 | — |
| `item_pipe` | logistics | ADR 0030 | — |
| `fluid_pipe` | logistics | ADR 0030 | — |
| `wrench` | tool | Not tiered. Toggles machine faces, cycles pipe/cable connection modes, toggles the storage unit type lock | — |

All blocks above except `wrench` exist in the four tiers of ADR 0033.

## Consequences
- These identifiers are final for v1; renaming requires a superseding ADR and a migration plan.
- The data map `btgtech:geothermal_fuels` is registered by the `energy` feature.
