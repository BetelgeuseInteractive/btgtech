# 0017. Single Gradle project

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The build could be one Gradle project or several subprojects (for example `:api`, `:core`, `:content`). A public API may be offered later (planning topic 12).

## Options Considered
- **A. Single Gradle project:** simplest, closest to the official MDK; an API, if approved, is a dedicated package packaged as an extra jar (the approach used by AE2 and Mekanism). API boundaries are protected by rules and tests, not by the compiler.
- **B. Multi-project build:** compiler-enforced boundaries, but more build complexity with no concrete need today.

## Decision
Option A. BTG Tech is a single Gradle project with the standard `main` and `test` source sets. Additional source sets or subprojects require a new ADR backed by a concrete need.

## Consequences
- Boundaries between packages (ADR 0018) are enforced by architecture tests (planning topic 13).
