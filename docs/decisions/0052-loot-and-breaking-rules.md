# 0052. Loot and breaking rules

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
ADR 0035 left open whether face state and redstone mode survive breaking a machine. Ore and storage drops must also be fixed.

## Options Considered
Keeping energy and settings on machine items (more data components, more edge cases) versus resetting them (simplest).

## Decision

| Block | Drops when broken |
|---|---|
| Tin and lead ore | Raw ore; Fortune works as in vanilla; Silk Touch drops the ore block |
| Machines, generators, batteries | The block's item; inventory contents drop into the world; **stored energy, face state and redstone mode are reset** |
| Item storage unit, fluid tank | An item keeping its contents (ADR 0029) |
| Cables, pipes, material blocks | Themselves |

Sneak-wrench dismantling (ADR 0046) produces exactly the same drops.

## Consequences
- Batteries lose their charge when broken; players are informed by the battery tooltip.
