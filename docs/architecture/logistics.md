# Item and Fluid Storage and Transport

Design of the item storage unit, fluid tank, item pipes and fluid pipes. Decisions: ADR [0029](../decisions/0029-item-and-fluid-storage-blocks.md), [0030](../decisions/0030-instant-transfer-pipe-networks.md), [0031](../decisions/0031-shared-network-infrastructure.md). Shared network rules: [energy.md §3](energy.md#3-cable-networks).

## 1. Common Model

- Items: `ResourceHandler<ItemResource>` via `Capabilities.Item.BLOCK`.
- Fluids: `ResourceHandler<FluidResource>` via `Capabilities.Fluid.BLOCK`; amounts in mB.
- Every multi-step operation runs in a transaction and commits only if all steps succeed.
- Prefer NeoForge helpers (`ResourceHandlerUtil.move`, `FluidUtil`) over hand-written transfer loops.

## 2. Storage Blocks (`storage` feature)

| | Item storage unit | Fluid tank |
|---|---|---|
| Contents | One item type, large count | One fluid |
| Handler | BTG single-slot handler with large counter | BTG single-fluid handler |
| Capacity | Per tier, balance table (planning topic 8) | Per tier in mB, balance table |
| Player interaction | Defined in planning topic 9 | Right-click with bucket / fluid container (`FluidUtil`) |
| Type lock | Optional, toggled with the wrench; keeps the item type when empty | — |
| When broken | Drops as an item carrying contents (and lock state) in a data component; tooltip shows contents | Same |

Contents are never spilled into the world.

## 3. Pipe Networks (`logistics` feature)

### 3.1 Structure
- Pipes have no block entity and hold no contents. Per-side connection state is stored in the block state.
- Each endpoint connection has a mode: `insert` (default) → `extract` → `disabled`, cycled with the wrench.
- Pipes connect only to pipes of the same kind (item or fluid) and the same tier.

### 3.2 Transfer Algorithm
- **Item network:** every operation interval (tier dependent), for each `extract` endpoint, extract up to the tier's item count and insert it into `insert` endpoints in round-robin order; anything not accepted stays in the source.
- **Fluid network:** every tick, for each `extract` endpoint, move up to the tier's mB per tick to `insert` endpoints in round-robin order.
- Extraction and insertion happen in one transaction, so nothing is lost or duplicated.
- No filters or priorities in v1.

### 3.3 Code Placement
- Graph structure and merge/split: `core.network.logic`; per-level manager and tick hook: `core.network` (ADR 0031).
- Item and fluid transfer strategies, pipe blocks: `logistics`.
- Pipe rendering: `logistics.client`.

## 4. Extension Points

- Filters, priorities, multi-pipes: later versions, each with a new ADR; they plug into the transfer strategy.
- Cross-dimension transport (space technology): a new endpoint block; the graph infrastructure stays unchanged.
