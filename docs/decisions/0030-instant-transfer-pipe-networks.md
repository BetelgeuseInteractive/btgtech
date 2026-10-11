# 0030. Instant-transfer pipe networks

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
v1 includes item pipes and fluid pipes in four tiers (ADR 0006, 0007). Unlike energy endpoints, inventories such as chests both accept and provide resources, so roles cannot be inferred by simulation without resources bouncing between inventories.

## Options Considered
- **A. Instant transfer:** the network periodically extracts from sources and inserts directly into destinations; nothing is inside the pipes. Best performance, no in-flight state to save, items cannot be lost; consistent with cable networks (ADR 0028).
- **B. Visible travelling items** (BuildCraft/Mekanism style): per-item state, persistence, rendering and rerouting; complex and expensive.

## Decision
Option A, with these rules:
1. **No pipe block entities and no buffered contents**; pipes follow the cable structure of ADR 0028 (per-side connection state in the block state, networks rebuilt from the world, nothing persisted).
2. **Connection modes** per endpoint connection: `insert` (default; the block is a destination) → `extract` (the block is a source) → `disabled`, cycled with the wrench.
3. **Tiers:** pipes of different tiers never connect. The tier sets the extraction rate: for item pipes, items per operation and operation interval; for fluid pipes, mB per tick. Values come from the balance table (planning topic 8) via the `SERVER` config.
4. **Distribution:** round-robin across destinations that accept the resource.
5. **No filters in v1** (ADR 0006): any extracted resource may go to any destination accepting it.
6. Item pipes and fluid pipes are separate blocks and separate networks; there is no combined pipe.
7. Visual feedback for flow, if any, is decided in planning topic 9.

## Consequences
- Players control flow direction explicitly with the wrench.
- Filters, priorities and multi-pipes are candidates for later versions and need new ADRs.
