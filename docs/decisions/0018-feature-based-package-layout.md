# 0018. Feature-based package layout

- **Status:** Accepted (amended by [0075](0075-data-generation-may-use-client-classes.md))
- **Date:** 2026-10-11

## Context
v1 contains about 55 blocks across several systems (ADR 0006), and space technology will be added later. Code must have one obvious home, client-only code must stay isolated (ADR 0009) and Minecraft-independent logic must be separable (ADR 0011).

## Options Considered
- **A. By feature** (`energy`, `machine`, `storage`, …): everything a feature needs lives together; easy to read, port and extend with new features.
- **B. By type** (`block`, `item`, `blockentity`, `menu`, …): common in small mods; one feature is scattered over many packages.

For enforcing the `logic` rule: an architecture test (simple) versus a separate source set without Minecraft on the classpath (compile-time, but more build complexity).

## Decision
Option A, under the package root `com.betelgeuseinteractive.btgtech` (ADR 0016):

| Package | Responsibility |
|---|---|
| `BTGTech` (class) | `@Mod` entry point |
| `core` | Shared infrastructure: tiers, registration helpers, networking base (detailed in planning topic 5) |
| `energy` | Generators, batteries, cables |
| `machine` | Machine framework and processing machines |
| `storage` | Item storage unit, fluid tank |
| `logistics` | Item and fluid pipes |
| `material` | Ores, alloys, world generation |
| `tool` | Wrench |
| `datagen` | Data generation providers |

Rules:
1. **`client` sub-packages:** each feature keeps client-only code (screens, renderers, client setup) in its own `client` sub-package. No class outside a `client` package may depend on a `client` package.
2. **`logic` sub-packages:** Minecraft-independent logic lives in a feature's `logic` sub-package. Classes in `logic` packages must not import `net.minecraft..` or `net.neoforged..`.
3. Both rules are enforced by automated architecture tests (planning topic 13).
4. New top-level packages (`api`, `compat`, `space`, …) are created only when their planning topic is approved and real code needs them. Empty packages are not created.
5. Package names are lowercase, singular, single English words where possible.

## Consequences
- The feature list above is fixed for v1; a new top-level feature package needs an approved ADR.
- Registry placement and the contents of `core` are decided in planning topic 5.
