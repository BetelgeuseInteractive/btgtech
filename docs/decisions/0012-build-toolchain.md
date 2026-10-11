# 0012. Build toolchain

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The build toolchain must match Minecraft 26.1.2 / NeoForge requirements and stay simple and close to the official examples.

## Options Considered
- Build plugin: **ModDevGradle (MDG)** (simpler, faster, first to support NeoForge snapshots) vs. **NeoGradle** (more features we do not need).
- Gradle version: the official MDK's **9.2.1** (known to work) vs. the latest **9.8.1** (compatibility with MDG not verified on 2026-10-11).
- Build script language: **Groovy DSL** (used by the official MDK and docs) vs. Kotlin DSL (type-safe, but diverges from official examples).

## Decision
- **Java 25**, pinned through the Gradle Java toolchain.
- **ModDevGradle 2.0.148** (latest on 2026-10-11).
- **Gradle 9.2.1** through the Gradle Wrapper; no global Gradle installation. Upgrading Gradle is a separate, verified step.
- **Groovy DSL** for build scripts.
- The project starts from the official **`NeoForgeMDKs/MDK-26.1.2-ModDevGradle`** template, adapted to the approved identity and structure.

## Consequences
- Tool versions are pinned and only updated deliberately, each update verified by a successful build and game launch.
- The environment setup document (planning topic 3) describes the exact steps.
