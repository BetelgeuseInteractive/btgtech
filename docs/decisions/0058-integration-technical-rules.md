# 0058. Integration technical rules

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Integrations must never make BTG Tech depend on another mod being present, and they must not break when the other mod updates. JEI and Jade discover plugins through their own annotations (`@JeiPlugin`, `@WailaPlugin`).

## Options Considered
The rules below were proposed as one set and approved, including an exception to ADR 0022 for annotation-based plugin discovery required by those mods' APIs.

## Decision
1. Every integration is **optional**: the game runs without the integrated mod.
2. Integration code lives in **`compat.<modid>`** (e.g. `compat.jei`, `compat.jade`) and is only loaded when that mod is present.
3. Builds compile against the integrated mod's API only; the full mod is loaded only in the development runtime for testing.
4. Each integrated mod is declared in `neoforge.mods.toml` as an **optional dependency with a version range**, so incompatible versions cannot crash BTG Tech.
5. **Exception to ADR 0022:** annotation-based plugin discovery is allowed where the integrated mod's API requires it (`@JeiPlugin`, `@WailaPlugin`), and only there.
6. BTG recipe types are synced to clients (ADR 0037) so recipe viewers can display them.

## Consequences
- ADR 0022 is amended by rule 5.
- The `compat` top-level package is created with the first integration (ADR 0018).
