# 0025. Configuration policy

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
NeoForge 26.1 provides `STARTUP`, `CLIENT`, `COMMON` and `SERVER` configs. Only `SERVER` configs are synchronized to clients and can be overridden per world.

## Options Considered
The usage table below was proposed and approved; the main alternative, allowing content to be toggled by config, was rejected because it risks registry mismatches and client/server desynchronization.

## Decision

| Config type | Use |
|---|---|
| `SERVER` | All balance values: generation rates, capacities, transfer rates, processing times |
| `CLIENT` | Visual and UI preferences |
| `COMMON` | Only with a concrete, approved need |
| `STARTUP` | Not used |

Content (blocks, items, machines) cannot be disabled through configuration. Server owners can disable recipes with datapacks.

## Consequences
- Balance values are read from the `SERVER` config at runtime, never cached in static fields at registration time.
- Which stats are configurable is decided per system in planning topics 6–8.
