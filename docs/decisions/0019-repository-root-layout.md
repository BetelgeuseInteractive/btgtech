# 0019. Repository root layout

- **Status:** Accepted (amended by [0067](0067-repository-files.md))
- **Date:** 2026-10-11

## Context
The repository root holds planning documents, the Gradle build, mod sources, licenses and art source files. Each must have a fixed, predictable location.

## Options Considered
The layout follows the official NeoForge MDK with additions for documentation, licenses and art sources; no competing layout was proposed.

## Decision

| Path | Content |
|---|---|
| `docs/` | Planning and project documentation (ADR 0020) |
| `src/main/java/` | Mod source code (ADR 0018) |
| `src/main/resources/` | Hand-written resources (e.g. textures) |
| `src/main/templates/` | `META-INF/neoforge.mods.toml` template (MDK layout) |
| `src/generated/resources/` | Data generation output (whether it is committed is decided in planning topics 10 and 14) |
| `src/test/java/` | Unit and architecture tests (planning topic 13) |
| `gradle/`, `build.gradle`, `settings.gradle`, `gradle.properties`, `gradlew`, `gradlew.bat` | Gradle build and wrapper |
| `LICENSE` | MPL-2.0 license text (ADR 0013) |
| `LICENSE-ASSETS.md` | All Rights Reserved asset license with modpack and resource pack permissions (ADR 0013) |
| `README.md` | Public project introduction |
| `art/` | Source art files (Blockbench projects, pixel-art sources); never packaged into the jar |

## Consequences
- Folders are created when their first real file exists (`art/` with the first art source, `src/test/java/` with the first test).
- Files not listed here need an approved decision before being added at the root.
