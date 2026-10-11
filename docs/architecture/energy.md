# Energy System

Design of energy storage and transport in BTG Tech. Decisions: ADR [0026](../decisions/0026-energy-model.md), [0027](../decisions/0027-soft-tier-bottleneck.md), [0028](../decisions/0028-graph-based-cable-networks.md). Generator, battery and machine behavior is part of the machine framework (planning topic 8).

## 1. Model

- Single abstraction: NeoForge `EnergyHandler`; unit FE.
- All BTG energy blocks store energy in one shared storage class derived from `SimpleEnergyHandler`, which:
  - enforces the block's tier capacity and per-operation limits,
  - marks the owning block entity dirty and requests synchronization on change,
  - is serialized with `ValueInput` / `ValueOutput`.
- Exposure: `Capabilities.Energy.BLOCK`, registered per block entity type in `RegisterCapabilitiesEvent`.
- Every multi-step operation is wrapped in a transaction; nothing is committed unless all steps succeed. Simulation = an uncommitted transaction.

## 2. Tiers

Tiers limit throughput and capacity only (soft bottleneck). Mixing tiers never destroys or damages anything. Values come from the `SERVER` config (ADR 0025); defaults are defined in the balance table (planning topic 8).

## 3. Cable Networks

### 3.1 Structure
- A cable block has no block entity. Its block state holds, per side, `connected`, `not connected` or `disabled` (set with the wrench).
- Cables connect only to cables of the same tier and to blocks exposing `Capabilities.Energy.BLOCK` on that side.
- A **network** is a connected set of same-tier cables plus the endpoints adjacent to them.

### 3.2 Lifecycle
- Networks exist only in memory, managed by one network manager per server level.
- Placing a cable joins or merges networks; removing one may split a network. Updates are incremental.
- On chunk load, networks are rebuilt lazily from block states. Nothing is persisted, because cables hold no energy.
- Endpoints in unloaded chunks are skipped; their `BlockCapabilityCache` reports them as unavailable.

### 3.3 Tick Algorithm (once per network per server tick)
1. Classify endpoints by simulation: extract-only = source, insert-only = consumer, both = buffer.
2. Budget = the network tier's throughput per tick.
3. Move energy from sources to consumers, split evenly across consumers.
4. Move remaining source output into buffers.
5. If consumers are still unsatisfied, move energy from buffers to consumers.
6. Buffers never send energy to other buffers.

### 3.4 Code Placement
- Generic graph structure and merge/split: `core.network.logic`; per-level network manager, tick hook and endpoint capability caches: `core.network` (ADR 0031).
- Energy distribution algorithm (steps 1–6 above): `energy.logic` (no Minecraft imports, unit-tested).
- Cable block and energy transfer strategy: `energy`.
- Cable rendering: `energy.client`.

## 4. Extension Points

- New energy sources: new generator blocks built on the machine framework; the network needs no changes.
- New dimensions (space technology): networks are per level, so they work in any dimension without changes.
- Item energy (portable batteries): later, via NeoForge item energy support (ADR 0026).
