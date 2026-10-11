# 0035. Common machine features

- **Status:** Accepted (amended by [0041](0041-generators-push-energy-to-neighbors.md))
- **Date:** 2026-10-11

## Context
All machine-like blocks share a set of behaviors. The owner does not want cables or pipes attached to a machine's front face because it spoils its look.

## Options Considered
For side access: **A.** front face always closed; **B.** front closed by default plus per-face open/closed toggling with the wrench; **C.** full per-face, per-resource side configuration GUI (Mekanism/Thermal style), too heavy for v1. Other features (redstone control, upgrade cards, auto-eject) were proposed and decided individually as listed below.

## Decision
Applies to **all machine-like blocks**: processing machines, generators, batteries, item storage units and fluid tanks.

| Feature | Rule |
|---|---|
| Facing | Every machine-like block has a front face, facing the player when placed |
| Side access | Each face is `open` or `closed`. The front is `closed` by default, all other faces `open`. Clicking a face with the wrench toggles it. A closed face exposes no capabilities, so cables and pipes neither connect nor render a connection there |
| Slot roles | On open faces, input slots accept insertion only and output slots allow extraction only |
| Face state | Stored in the block entity; every change calls `level.invalidateCapabilities(pos)` |
| Wrench targets | Clicking a pipe or cable changes its connection mode (ADR 0030); clicking a machine face toggles that face |
| Auto-eject | None; pipes in `extract` mode pull outputs |
| Redstone control | Three modes: ignore, active with signal, active without signal |
| Upgrade cards | None; tiers provide progression (ADR 0007) |
| Energy use | Consumed per tick only while processing; no idle drain |
| Idle behavior | A machine whose inventory has not changed does not search for recipes; client sync happens only on change |

## Consequences
- Full per-face, per-resource side configuration is a candidate for a later version and needs its own ADR.
- Face state and redstone mode are kept when the block is broken only if planning topic 9/10 decides so; by default they reset.
