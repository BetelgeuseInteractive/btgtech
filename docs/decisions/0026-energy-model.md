# 0026. Energy model

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The owner requires energy to interoperate through NeoForge's common energy capability. Since the NeoForge 21.9 transfer rework, that capability is `EnergyHandler` (`Capabilities.Energy.BLOCK` / `ITEM` / `ENTITY`): amounts are readable as `long`, while `insert` / `extract` take an `int` and a `TransactionContext` (simulation = an uncommitted transaction). NeoForge ships `SimpleEnergyHandler` (int capacity, serialized with `ValueInput` / `ValueOutput`). The interface is identical on the 26.1.x and 26.3.x branches (checked 2026-10-11).

## Options Considered
- **A. NeoForge `EnergyHandler` directly, unit FE:** native interoperability, least code.
- **B. Own energy API with an adapter to NeoForge:** full control, but two layers to maintain and conversion bugs; no concrete need.

## Decision
Option A:
- BTG Tech uses NeoForge `EnergyHandler` as its only energy abstraction. The unit is **FE**; there is no BTG-specific unit or conversion.
- One BTG storage class extends `SimpleEnergyHandler`; it applies tier limits and, on change, marks the owning block entity dirty and triggers synchronization. All BTG energy blocks use it.
- Energy is exposed through `Capabilities.Energy.BLOCK` registered in `RegisterCapabilitiesEvent`. Neighbor lookups use `BlockCapabilityCache`; `level.invalidateCapabilities(pos)` is called whenever a block capability changes, appears or disappears outside the cases NeoForge handles automatically.
- Every multi-step energy operation runs inside a transaction and commits only if all steps succeed.

## Consequences
- Per-tier capacity is limited to `int` (about 2.1 billion FE), sufficient for v1.
- Item energy (portable batteries, charging) is out of v1 scope (ADR 0006); when added, it uses NeoForge's item energy support (e.g. `ItemAccessEnergyHandler`).
