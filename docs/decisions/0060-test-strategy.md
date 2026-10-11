# 0060. Test strategy

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Quality assurance must rely mainly on automated checks. Several architecture rules (ADR 0018, 0023, 0058) require automated enforcement. ModDevGradle supports JUnit unit tests (`neoForge.unitTest { enable() }`), optionally inside a real server via the NeoForge test framework (`EphemeralTestServerProvider`). NeoForge 26.1 game tests are data-driven (`test_instance`, `test_environment`, generatable by data generation) and run inside a structure template.

## Options Considered
The layered strategy below was proposed as one set and approved. For game test structures, one empty platform template with blocks placed by code was chosen over one hand-made structure per test.

## Decision

| Layer | Tool | Covers | Runs |
|---|---|---|---|
| Unit tests | JUnit, no Minecraft | `logic` packages: network graph and split/merge, energy distribution, tier math | Every `gradlew build` |
| Architecture tests | ArchUnit | `client` isolation; `logic` purity; `core` independent of features; no feature cycles; JEI/Jade references only in `compat`; plugin annotations only in `compat` | Every `gradlew build` |
| Data checks | JUnit | Every `en_us` key present in all languages (ADR 0051); every block has a model, loot table and translation | Every `gradlew build` |
| Game tests | NeoForge GameTest (`runGameTestServer`) | In-world behavior: processing with energy, cable distribution, pipe transfer, storage contents kept on break, generator push, face toggling | End of each work package and in CI |
| Manual tests | `runClient`, `runServer` | Visual checks; dedicated-server verification before v1.0 | On visual changes; before v1.0 |

- Game tests use **one empty platform structure template**; tests place blocks by code. This adds one structure file to the hand-made resources of ADR 0050; whether it is hand-made or generated is settled in Phase 1.
- Every public method in a `logic` package has a unit test. No coverage tool is used in v1.

## Consequences
- ADR 0050 is amended by the structure template exception.
- Architecture tests turn several ADR rules into build failures.
