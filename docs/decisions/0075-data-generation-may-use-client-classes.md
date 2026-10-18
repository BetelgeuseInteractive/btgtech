# 0075. Data generation may use client classes

- **Status:** Accepted
- **Date:** 2026-10-18

## Context
ADR 0018 forbids any class outside a `client` package to depend on a `client` package, and the architecture tests enforce it. In Minecraft 26.1, the model provider and its helpers live in `net.minecraft.client.data.models`, and NeoForge's sprite source provider, needed for the runtime recoloring of ADR 0074, lives in `net.neoforged.neoforge.client.data`. The `datagen` package (ADR 0050) must use them for the first block and item models.

`datagen` is reachable only from the client: `BTGTechClient` registers `DataGenerators`, and all providers run on `GatherDataEvent.Client` (ADR 0050). It never runs on a dedicated server.

## Options Considered
- **A. Treat `datagen` like client code:** the client isolation rule exempts `datagen`; a new rule allows only `client` packages to reference `datagen`. The package layout stays as ADR 0018 and ADR 0050 define it.
- **B. Move all providers into a `datagen.client` sub-package:** no rule change, but the package names and the wording of ADR 0050 and the documents change.

## Decision
Option A:
- Classes in the `datagen` package may depend on client packages.
- Only classes in `client` packages may depend on the `datagen` package; common and server code never reaches it.
- Both rules are architecture tests (testing.md, section 2).

## Consequences
- ADR 0018 is amended by this ADR: its client isolation rule has the `datagen` exception.
- `datagen` must stay free of code that common code would need; shared constants such as translation keys live in the owning feature or in `core`.
