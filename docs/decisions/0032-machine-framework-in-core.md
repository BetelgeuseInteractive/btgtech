# 0032. Machine framework in `core`

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Generators and batteries (`energy`) and processing machines (`machine`) share one machine framework (GUI base, tiers, registration helpers, synchronization), while machines consume energy. Placing the framework in `machine` would create an `energy ↔ machine` cycle, which ADR 0023 forbids.

## Options Considered
Framework in `machine` (cycle with `energy`) versus framework in `core` (two users, satisfies ADR 0023).

## Decision
- The machine framework lives in **`core.machine`**.
- The BTG energy storage class derived from `SimpleEnergyHandler` (ADR 0026) is part of this framework and lives in `core.machine` as well.
- `machine` contains only processing machines; `energy` contains generators, batteries and cables; `storage` may use the framework where it fits. All depend on `core`, none on each other through the framework.

## Consequences
- ADR 0026's rules are unchanged; only the location of the storage class is fixed here.
- `core.machine` must stay generic: machine-specific behavior belongs to the feature that owns the machine.
