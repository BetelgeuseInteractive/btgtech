# Machine Framework

Design of all machine-like blocks: processing machines, generators, batteries, item storage units and fluid tanks. Decisions: ADR [0032](../decisions/0032-machine-framework-in-core.md)–[0043](../decisions/0043-tier-scaling-and-balance-defaults.md). Content list: ADR [0039](../decisions/0039-v1-content-identifiers-and-behavior.md). Values: [balance.md](balance.md).

## 1. Structure (`core.machine`)

| Element | Responsibility |
|---|---|
| `MachineBlock` (abstract) | One block per tier; `facing` and `active` properties, front towards the player on placement, right-click opens the GUI if the block entity is a `MenuProvider`, server ticker (`ticksOnServer()` is `false` for passive blocks) |
| `MachineBlockEntity` (abstract) | Common lifecycle: ticking, saving/loading, face states, redstone mode, processing progress, delayed active state, spilling slots on removal. Tier and definition come from the block, so one block entity type serves all four tiers. Subclass depth at most two levels |
| `MachineDefinition` | Data object per machine: slot layout and the components used; tanks, energy use and recipe type are added with the work packages that need them. Tier values are functions, read when the block entity is created (`SERVER` config, ADR 0025) |
| Components | `MachineEnergyStorage` (derived from `SimpleEnergyHandler`), `MachineItemSlots` (inputs, then outputs), `FaceStates`, and in `core.machine.logic` `ProcessingProgress`, `RedstoneMode`, `ActiveState`; fluid tanks follow in WP-201 |
| `MachineMenu` (abstract) | Machine slots, standard player inventory, shift-click, redstone mode button, data slots for energy, capacity, progress, duration and redstone mode |
| `MachineCapabilities` | Registers the face-aware energy and item handlers of a machine block entity type |
| `MachineScreen` (abstract, `core.machine.client`) | Screen base with the tier panel, slot frames, energy bar, redstone mode button and title; widgets and layout in [gui.md](gui.md) |
| Recipe base | Shared recipe classes and codecs for all BTG recipe types (WP-106) |

Rules for machine code:
- The block itself produces and uses energy with `generate` and `consume`, which ignore the transfer limits; other blocks go through the energy handler and its tier limits.
- Other blocks see the item slots through the sided view: insertion into inputs only, extraction from outputs only.
- A processing machine calls `consumeInputChange()` before looking up a recipe and stays idle while it returns `false` (idle sleep).
- Visible activity goes through `setActive`, which keeps the `active` state for at least 20 ticks (ADR 0047).

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
