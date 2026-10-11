# 0011. One branch per Minecraft version

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The owner wants to support multiple Minecraft 26.x versions. The official NeoForge migration primers show large breaking changes in every 26.x drop (26.2: rendering overhaul; 26.3: client rewrite, reloadable datapack registries, datagen provider changes), so one unmodified source tree cannot compile for all versions.

## Options Considered
- **A. One Git branch per Minecraft version**, porting features between branches. Each branch is clean standard NeoForge code; every feature must be ported to each supported line.
- **B. Stonecutter single-source preprocessing.** One commit reaches all versions, but large API differences fill the code with conditional blocks; adds a third-party preprocessor and makes the code harder to read and debug.
- **C. Own abstraction layer** (version-independent core plus per-version adapter modules). Lowest theoretical porting cost, but the Minecraft API surface used by a tech mod is so large that the layer becomes a second framework.

## Decision
Option A:
- Each supported Minecraft version has its own long-lived branch. Branch naming is fixed in planning topic 14.
- Pure game logic (energy arithmetic, network graph algorithms, tier statistics, recipe matching logic) lives, wherever natural, in packages that do not reference Minecraft classes. Such code ports unchanged and is unit-testable.
- Ports begin after the v1 core is complete (ADR 0006). The support policy for each line (features vs. fixes only) is decided in planning topic 14.

## Consequences
- The package structure (planning topic 4) must provide a clear home for Minecraft-independent logic.
- Porting effort grows with the number of supported lines; the support policy must keep it bounded.
