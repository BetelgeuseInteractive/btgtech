# 0031. Shared network infrastructure

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Energy cables (ADR 0028), item pipes and fluid pipes (ADR 0030) use the same graph structure. ADR 0028 rule 7 and ADR 0023 (code enters `core` once it has two users) require moving the shared part to `core`.

## Options Considered
Keeping a copy of the graph code in each feature (duplication) versus one shared implementation in `core` with per-feature transfer strategies.

## Decision
- The generic network graph (nodes, incremental merge and split, network membership) lives in **`core.network.logic`** as Minecraft-independent, unit-tested code.
- The Minecraft-facing part (per-level network manager, server tick hook, chunk load handling, capability caches for endpoints) lives in **`core.network`**.
- Each feature supplies only its **transfer strategy**: energy distribution in `energy`, item and fluid transfer in `logistics`.
- The rules of ADR 0028 and ADR 0030 are unchanged; this ADR only fixes where the code lives.

## Consequences
- `core.network` refers to transport networks only. Packet and synchronization code (planning topic 11) must use a different package name to avoid confusion.
- New network types (e.g. future space-technology transport) reuse this infrastructure by adding a transfer strategy.
