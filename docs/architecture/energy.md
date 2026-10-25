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

## 4. Solid Fuel Generator

- Burns items with a burn time in the `neoforge:furnace_fuels` data map, one at a time, from its single fuel slot, which accepts fuel only; a lava bucket leaves its empty bucket in the slot.
- A fuel holds its burn time times the energy per burn tick (40 FE) on every tier; each tick releases at most the tier's output (`energy.logic.FuelBurner`), so higher tiers burn the same fuel faster, never more efficiently (ADR 0043).
- Burns only while its buffer has room and its redstone mode allows operation; fuel is never wasted.
- Other blocks may extract up to the cable throughput of its tier; nothing can be inserted. It does not push yet (WP-204).
- The front is active while it produces energy (ADR 0047). The screen shows the fuel slot, a flame for the remaining fuel and the energy bar.

## 5. Battery

- Values from the `SERVER` config (`BalanceConfig`): capacity, and a transfer rate per direction equal to the cable throughput of its tier, so a lower-tier battery limits a higher-tier network softly (ADR 0027).
- Passive: it never pushes energy (ADR 0041) and does not tick.
- Transfers only while its redstone mode allows operation (ADR 0035); reading the stored energy always works.
- Comparator output: 0 when empty, 15 when full, otherwise 1 plus the fill level scaled to 0–14, rounded down (`energy.logic.ComparatorSignal`).
- The front is active while energy flows and for 20 ticks after the last transfer (ADR 0047); a scheduled block tick ends the active state, so the battery needs no ticker.
- Broken, it drops itself and loses its energy (ADR 0052); its item tooltip says so.

## 6. Extension Points

- New energy sources: new generator blocks built on the machine framework; the network needs no changes.
- New dimensions (space technology): networks are per level, so they work in any dimension without changes.
- Environment factors (space technology): solar output is computed in exactly one method; a per-dimension environment factor will be applied there later (ADR 0071). v1 code never checks specific dimensions (ADR 0070).
- Item energy (portable batteries): later, via NeoForge item energy support (ADR 0026).
