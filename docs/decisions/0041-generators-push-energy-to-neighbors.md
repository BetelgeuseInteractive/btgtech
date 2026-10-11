# 0041. Generators push energy to neighbors

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
ADR 0035 states that machines do not auto-eject. For energy this means a generator placed directly next to a machine would not power it without a cable, contrary to player expectations.

## Options Considered
Keeping "no auto-eject" for energy (cables always required) versus letting generators push energy to adjacent consumers.

## Decision
Amends ADR 0035 for energy only:
- Every tick, **generators** push energy through their open faces into adjacent blocks that accept energy.
- Generators do **not** push into BTG energy cables; cable networks already pull from them (ADR 0028), so energy is never handled twice.
- **Batteries do not push**; they remain passive buffers, which prevents battery-to-battery oscillation.

## Consequences
- ADR 0035's "no auto-eject" rule continues to apply to items and fluids.
