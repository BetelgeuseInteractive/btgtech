# 0040. Tiered crafting components

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
ADR 0006 did not list intermediate crafting components. A four-tier progression of medium depth needs consistent, readable crafting steps.

## Options Considered
- **A. Machine casing and circuit per tier (8 items):** consistent, readable progression; stronger tech-mod feel.
- **B. No components:** recipes use ingots and vanilla materials directly; simpler but flat.

## Decision
Option A. v1 adds two crafting components per tier:
- `basic_machine_casing`, `advanced_machine_casing`, `elite_machine_casing`, `ultimate_machine_casing`
- `basic_circuit`, `advanced_circuit`, `elite_circuit`, `ultimate_circuit`

Every tiered machine-like block of a tier is crafted with that tier's casing and/or circuit. This ADR extends the v1 content list of ADR 0006.

## Consequences
- Component recipes define the material gate of each tier (ADR 0038); exact recipes are defined during content data generation (planning topic 10).
- Components are used by `energy`, `machine` and `storage`, so they are registered in `core` (ADR 0023).
