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
- **Scope:** `MachineBlockEntity`, machine definitions, components (BTG energy storage class, item slots, processing progress, redstone control, face state), facing, slot roles, idle sleep, capability exposure and the menu base with its synchronization.
- **Acceptance:** unit tests for component logic where Minecraft-independent; verified through WP-105/106 game tests.
- **Depends on:** WP-004.
- **References:** ADR 0032, 0034, 0035, 0044, 0045, [machines.md](../architecture/machines.md), [gui.md](../architecture/gui.md).
- **Changed on 2026-10-17 (owner approval):** the screen and widget set moved to WP-109, so that each part fits one reviewable pull request.

## WP-109 Machine screen and widgets

- **Goal:** the shared machine screen.
- **Scope:** the screen base with the standard layout; the widget set (energy bar, progress arrow, redstone mode button, tooltip helpers with SI formatting); GUI sprites drawn per the art style guide and reviewed by the owner.
- **Acceptance:** unit tests for Minecraft-independent formatting logic; checked visually with the first machine GUI in WP-105.
- **Depends on:** WP-102.
- **References:** ADR 0044, 0045, 0074, [gui.md](../architecture/gui.md), [art style guide](../development/art-style-guide.md).

## WP-104 Materials part 1

- **Goal:** materials needed by the slice.
- **Scope:** tin (stone and deepslate ore, raw, ingot, nugget, dust, plate, storage block, raw storage block) with world generation; bronze (ingot, nugget, dust, plate, storage block); `basic_machine_casing`, `basic_circuit`; `c:` tags, loot, models, recipes, translations.
- **Acceptance:** tin generates per balance table; tags and loot verified by data checks.
- **Depends on:** WP-004, WP-005.
- **References:** ADR 0038, 0040, 0052, 0053, [balance.md §5](../architecture/balance.md#5-ore-generation-overworld).

## WP-105 Energy slice

- **Goal:** energy produced, stored and transported.
- **Scope:** `solid_fuel_generator`, `battery`, `energy_cable`, each in four tiers; cable energy distribution strategy (`energy.logic`); batteries passive; balance values from the `SERVER` config, which `CoreFeature` registers from this work package on (ADR 0025).
- **Acceptance:** unit tests for the distribution algorithm; game tests: generator charges a battery through a cable; tier throughput limit holds; `runClient`: a GUI shows an energy value above 32,767 correctly (ADR 0073).
- **Depends on:** WP-101, WP-102, WP-104, WP-109.
- **References:** ADR 0025, 0026, 0027, 0028, 0043, 0073, [balance.md](../architecture/balance.md).

## WP-103 Wrench

- **Goal:** the single configuration tool.
- **Scope:** `wrench` item: toggle machine faces, sneak-dismantle (same drops as breaking), cycle the energy cable's side mode; a wrench hook in `core` that pipes (WP-402, WP-403) and the storage type lock (WP-404) implement later.
- **Acceptance:** game tests for face toggling, dismantling and the cable side mode.
- **Depends on:** WP-102, WP-105.
- **References:** ADR 0035, 0039, 0046, 0052.
- **Changed on 2026-10-18 (owner approval):** moved after WP-105, so that its game tests use the first machine-like blocks and it covers the energy cable, which did not exist before WP-105.

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
- **Depends on:** WP-101 … WP-107, WP-109.
- **References:** ADR 0051, 0064, 0066.
