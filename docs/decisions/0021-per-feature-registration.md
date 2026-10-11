# 0021. Per-feature registration

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
NeoForge 26.1 registers content through `DeferredRegister` (with the `Blocks` and `Items` variants and the `DeferredBlock` / `DeferredItem` holders). Content is organized by feature (ADR 0018), so registration must fit that layout without hidden class-loading pitfalls.

## Options Considered
- **A. Central holders in `core`** (`ModBlocks`, `ModItems`, …): conflicts with the feature-based layout.
- **B. Shared registers in `core`, entries in feature holder classes:** if a holder class is not loaded before registration, its entries are silently missing.
- **C. Each feature owns its registers:** one holder class per registry type per feature, each with its own `DeferredRegister` and a `register(IEventBus)` method. Calling `register` loads the class, so no hidden dependency exists.

## Decision
Option C:
- Each feature has one holder class per registry type it uses, named `<Feature><RegistryType>`, e.g. `EnergyBlocks`, `EnergyItems`, `EnergyBlockEntityTypes`, `EnergyMenuTypes`.
- Each holder class declares its own `DeferredRegister` and exposes `static void register(IEventBus modBus)`.
- The feature's bootstrap class (ADR 0022) calls the `register` method of each of its holder classes.

## Consequences
- Several `DeferredRegister` instances exist for the same registry and namespace. This is common practice but not explicitly stated in the NeoForge documentation; it is verified during the Phase 1 vertical slice. If it fails, a superseding ADR is required.
- Registry names stay globally unique within the `btgtech` namespace; naming rules are defined in the coding standards (planning topic 13).
