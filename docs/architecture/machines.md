# Machine Framework

Design of all machine-like blocks: processing machines, generators, batteries, item storage units and fluid tanks. Decisions: ADR [0032](../decisions/0032-machine-framework-in-core.md)–[0043](../decisions/0043-tier-scaling-and-balance-defaults.md). Content list: ADR [0039](../decisions/0039-v1-content-identifiers-and-behavior.md). Values: [balance.md](balance.md).

## 1. Structure (`core.machine`)

| Element | Responsibility |
|---|---|
| `MachineBlockEntity` (abstract) | Common lifecycle: ticking, saving/loading, synchronization requests, face state, redstone mode. Subclass depth at most two levels |
| Machine definition | Data object per machine: slot layout, tanks, energy use, recipe type, components used. Tier values are read from the `SERVER` config |
| Components | Energy (BTG storage class derived from `SimpleEnergyHandler`), item slots, fluid tanks, processing progress, redstone control |
| Recipe base | Shared recipe classes and codecs for all BTG recipe types |

A machine gets its own subclass only for behavior components cannot express (e.g. generators). Machine-specific logic stays in the owning feature (`energy`, `machine`, `storage`).

## 2. Common Behavior (all machine-like blocks)

- **Facing:** front faces the player on placement.
- **Faces:** each face is `open` or `closed`; front `closed` by default; the wrench toggles a face. Closed faces expose no capabilities, so cables and pipes neither connect nor render there. Every change calls `level.invalidateCapabilities(pos)`.
- **Slot roles:** input slots accept insertion only, output slots allow extraction only.
- **No auto-eject** for items and fluids; pipes in `extract` mode pull outputs.
- **Generators push energy** each tick through open faces into adjacent energy acceptors, except BTG cables. Batteries are passive.
- **Redstone modes:** ignore / active with signal / active without signal.
- **No upgrade cards.** Tiers provide progression.

## 3. Processing Cycle

1. If inputs did not change since the last check and no recipe is active, do nothing (idle sleep).
2. On input change: look up a recipe (cached last match first). Reject recipes whose minimum tier is above the machine's tier.
3. While a recipe is active and redstone mode allows: consume energy per tick; advance progress; on completion, insert outputs (including chance byproducts) in one transaction. If outputs do not fit, pause without consuming energy.
4. On datapack reload, cached recipes are revalidated on the next tick.

Processing time = recipe base duration ÷ tier speed multiplier. Energy per operation is the same on every tier.

## 4. Recipes

| Machine | Recipe type |
|---|---|
| `electric_furnace` | `minecraft:smelting` |
| `crusher` | `btgtech:crushing` |
| `compressor` | `btgtech:compressing` |
| `alloy_smelter` | `btgtech:alloying` |
| `fluid_processor` | `btgtech:fluid_filling` / `btgtech:fluid_draining` (GUI mode switch) |

Fields: sized ingredients with `c:` tags, one or more outputs with optional chance byproducts, optional base duration, optional minimum tier (ADR 0036). BTG recipe types are sent to clients; machines do not use the vanilla recipe book (ADR 0037).

## 5. Tiers and Components

- Tiers: `basic`, `advanced`, `elite`, `ultimate`; one block per tier; IDs `<tier>_<block>` (ADR 0033).
- Each tier has a machine casing and a circuit (`<tier>_machine_casing`, `<tier>_circuit`), registered in `core` (ADR 0040).
- A higher-tier block is crafted from the previous tier plus that tier's components and materials.

## 6. Extension Points

- New machines: a definition, a recipe type if needed, and data.
- Multiblocks: a controller block entity owning components; specified in planning topic 15 (ADR 0042).
- Per-face, per-resource side configuration and in-world tier upgrade kits: later versions, each with its own ADR.
