# 0028. Graph-based cable networks

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Cable design dominates server tick cost in tech mods. Large bases contain thousands of cable blocks.

## Options Considered
- **A. Ticking cable block entities passing buffers:** simple; cost grows with every cable; energy moves slowly.
- **B. Graph-based networks:** connected cables form one network object that ticks once per tick; cost depends on endpoints, not cable count.
- **C. Path search per transfer:** flexible but very expensive.

## Decision
Option B, with these rules:
1. **Cables have no block entity.** Per-side connection state (connected / not connected / disabled by wrench) is stored in the block state.
2. **Cables of different tiers never connect.** Every network has exactly one tier; the network's total throughput per tick is set by that tier.
3. **Cables store no energy.** Transfer is instantaneous within the tick, so no network data is saved; networks are rebuilt from the world when loaded, and no energy is ever lost.
4. **Endpoint roles** are derived by simulation, so blocks from other mods work too: extract-only = source, insert-only = consumer, both = buffer. Each tick: sources → consumers; surplus → buffers; deficit → buffers → consumers. Buffers never transfer to other buffers.
5. **Fair distribution:** available energy is split evenly among consumers.
6. Graph algorithms live in `energy.logic` as Minecraft-independent, unit-tested code. A single per-level network manager ticks all networks on the server.
7. If item and fluid pipes (planning topic 7) adopt the same graph structure, the shared part moves to `core` (ADR 0023).

## Consequences
- Networks must be updated incrementally when cables are placed or removed (merge/split) and handle unloaded chunks safely.
- Endpoint handlers are accessed through `BlockCapabilityCache` (ADR 0026).
