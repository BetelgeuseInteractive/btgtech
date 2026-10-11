# Phase 1: Vertical Slice → `0.1.0`

Goal: one minimal, end-to-end path through every layer — network, framework, GUI, synchronization, recipes, data generation, translations, game tests — validating the architecture (ADR 0005).

## WP-101 Network infrastructure

- **Goal:** the shared transport network graph.
- **Scope:** `core.network.logic` (nodes, incremental merge and split, membership) and `core.network` (per-level manager, server tick hook, chunk load handling, endpoint capability caches).
- **Acceptance:** unit tests for merge/split and membership; no Minecraft imports in `core.network.logic` (architecture test).
- **Depends on:** WP-004.
- **References:** ADR 0028, 0031, [energy.md §3](../architecture/energy.md#3-cable-networks).

## WP-102 Machine framework

- **Goal:** everything machine-like blocks share.
- **Scope:** `MachineBlockEntity`, machine definitions, components (BTG energy storage class, item slots, processing progress, redstone control, face state), facing, slot roles, idle sleep, the menu base and the widget set (energy bar, progress arrow, redstone button, tooltip helpers) with the standard layout.
- **Acceptance:** unit tests for component logic where Minecraft-independent; verified through WP-105/106 game tests.
- **Depends on:** WP-004.
- **References:** ADR 0032, 0034, 0035, 0044, 0045, [machines.md](../architecture/machines.md), [gui.md](../architecture/gui.md).

## WP-103 Wrench

- **Goal:** the single configuration tool.
- **Scope:** `wrench` item: toggle machine faces, sneak-dismantle (same drops as breaking), cycle cable/pipe connection modes; storage type lock hook used later by WP-404.
- **Acceptance:** game tests for face toggling and dismantling.
- **Depends on:** WP-102.
- **References:** ADR 0035, 0039, 0046, 0052.

## WP-104 Materials part 1

- **Goal:** materials needed by the slice.
- **Scope:** tin (stone and deepslate ore, raw, ingot, nugget, dust, plate, storage block, raw storage block) with world generation; bronze (ingot, nugget, dust, plate, storage block); `basic_machine_casing`, `basic_circuit`; `c:` tags, loot, models, recipes, translations.
- **Acceptance:** tin generates per balance table; tags and loot verified by data checks.
- **Depends on:** WP-004, WP-005.
- **References:** ADR 0038, 0040, 0052, 0053, [balance.md §5](../architecture/balance.md#5-ore-generation-overworld).

## WP-105 Energy slice

- **Goal:** energy produced, stored and transported.
- **Scope:** `solid_fuel_generator`, `battery`, `energy_cable`, each in four tiers; cable energy distribution strategy (`energy.logic`); batteries passive; balance values from the `SERVER` config.
- **Acceptance:** unit tests for the distribution algorithm; game tests: generator charges a battery through a cable; tier throughput limit holds.
- **Depends on:** WP-101, WP-102, WP-104.
- **References:** ADR 0026, 0027, 0028, 0043, [balance.md](../architecture/balance.md).

## WP-106 Crusher and recipe base

- **Goal:** the first processing machine and the shared recipe infrastructure.
- **Scope:** `crusher` in four tiers; shared recipe base and codecs in `core.machine`; `btgtech:crushing` (sized ingredients, outputs with chance byproducts, optional processing time and minimum tier); recipe caching; client recipe sync; recipe book requirement resolved (ADR 0037).
- **Acceptance:** game test: crusher powered by the energy slice processes tin ore to dust.
- **Depends on:** WP-105.
- **References:** ADR 0036, 0037, 0039.

## WP-107 Item storage unit

- **Goal:** the first advanced storage block.
- **Scope:** `item_storage_unit` in four tiers; single-slot large-count handler; front-face insert/extract interactions; front display renderer; contents kept on break; block entity update throttling.
- **Acceptance:** game tests: insert/extract through capability; contents preserved after breaking and placing.
- **Depends on:** WP-102.
- **References:** ADR 0029, 0046, 0055, 0056.

## WP-108 Slice game tests, translations and 0.1.0

- **Goal:** release the vertical slice.
- **Scope:** fill game-test gaps; all slice keys in eight languages; `CHANGELOG.md`; release `0.1.0+mc26.1.2` as a GitHub pre-release; record architecture findings (and propose ADRs where a decision must change).
- **Acceptance:** release workflow publishes the pre-release; the owner approves.
- **Depends on:** WP-101 … WP-107.
- **References:** ADR 0051, 0064, 0066.
