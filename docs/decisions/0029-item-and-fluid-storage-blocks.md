# 0029. Item and fluid storage blocks

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
v1 includes a single-type high-capacity item storage unit and a fluid tank, each in four tiers (ADR 0006, 0007). Since the NeoForge transfer rework, items and fluids are exposed through `ResourceHandler<ItemResource>` and `ResourceHandler<FluidResource>` (`Capabilities.Item.BLOCK`, `Capabilities.Fluid.BLOCK`); amounts are readable as `long`, `insert` / `extract` take an `int` and a `TransactionContext`. The signatures are identical on 26.1.x and 26.3.x (checked 2026-10-11).

## Options Considered
Exposure through NeoForge `ResourceHandler` (consistent with ADR 0026, accessible to other mods' pipes and storage systems) versus a custom API; and, on breaking, dropping the contents versus keeping them in the item.

## Decision
- Both blocks expose their contents only through NeoForge `ResourceHandler` capabilities.
- **Item storage unit:** holds one item type in large quantity, using a BTG handler with a single slot and a large counter; capacity per tier from the balance table (planning topic 8). An optional **type lock** keeps the item type when the unit is empty; it is toggled with the wrench.
- **Fluid tank:** holds one fluid; capacity per tier in mB. Players fill and drain it by right-clicking with buckets and fluid containers (`FluidUtil`).
- **Contents are kept when broken:** the dropped item carries the contents in a data component, and its tooltip shows them. Contents are never spilled into the world.
- All multi-step operations run in transactions (as in ADR 0026).

## Consequences
- The stored contents must be preserved exactly when converting between block entity and item, including type lock state.
- Data components for stored contents are registered by the `storage` feature.
