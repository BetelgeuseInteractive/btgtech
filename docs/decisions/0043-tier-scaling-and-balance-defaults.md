# 0043. Tier scaling and balance defaults

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
All numeric values live in the `SERVER` config (ADR 0025) and are tuned during Phase 5 playtesting. What must be fixed now is the scaling logic and a coherent starting point, anchored to vanilla (furnace: 200 ticks per item; coal: 1,600 ticks / 8 items; lava bucket: 20,000 ticks / 100 items; hopper: 2.5 items/s).

## Options Considered
- **A.** Capacity/throughput ×1/×4/×16/×64, speed ×1/×2/×4/×8.
- **B.** ×1/×2/×4/×8 and ×1/×1.5/×2/×3: gentle, little upgrade excitement.
- **C.** ×1/×10/×100/×1000 and ×1/×4/×16/×64: too steep.

## Decision
Option A, with these rules:
1. Capacity and throughput scale ×1 / ×4 / ×16 / ×64 (Basic / Advanced / Elite / Ultimate).
2. Processing speed scales ×1 / ×2 / ×4 / ×8.
3. **Energy per operation is constant across tiers**: higher tiers are faster, not more efficient. Energy per fuel item is likewise constant across generator tiers.
4. **Storage blocks** (item storage unit, fluid tank) scale ×1 / ×8 / ×64 / ×512.
5. The default values are those in [`docs/architecture/balance.md`](../architecture/balance.md) as of this ADR.

## Consequences
- Changing the scaling rules requires a new ADR. Changing individual default values during tuning requires owner approval and an entry in the change log of `balance.md`.
- All values must fit in `int` (largest v1 value: 8,192,000 mB).
