# GUI and Interaction

Standards for screens, information display and in-world interaction. Decisions: ADR [0044](../decisions/0044-gui-technology.md), [0045](../decisions/0045-gui-layout-tier-colors-and-information-display.md), [0046](../decisions/0046-in-world-interaction-standards.md). Visual style and art production: ADR [0047](../decisions/0047-art-technical-rules.md)–[0049](../decisions/0049-art-tools-and-style-selection.md).

## 1. Technology

- Vanilla container screens; no GUI library.
- 26.1 API: screens submit render state via `GuiGraphicsExtractor` / `extractRenderState`; GUI textures are sprites under `assets/btgtech/textures/gui/sprites/`, drawn with `blitSprite` (tiling and nine-slice available).
- Prefer sprites and vanilla widgets over custom drawing; every custom drawing routine is a port risk.

## 2. Widget Set (`core.machine.client`)

| Widget | Purpose |
|---|---|
| Energy bar | Stored / capacity, tooltip with exact values |
| Fluid gauge | Fluid type, amount / capacity |
| Progress arrow | Processing progress |
| Redstone mode button | Cycles ignore / with signal / without signal |
| Machine mode button | Machine-specific modes (e.g. `fluid_processor` fill/drain) |
| Tooltip helpers | Short line + `Shift` details; SI formatting |

## 3. Screen Layout

```
┌──────────────────────── 176 px ────────────────────────┐
│                     Title (centered)           [RS]    │  RS = redstone mode button
│ ┃E┃                                                     │  E  = energy bar (always left)
│ ┃ ┃      machine-specific slots, gauges, arrows        │
│ ┃ ┃                                                     │
│ ─────────────────────────────────────────────────────── │
│                 Player inventory (3 × 9)                │
│                 Hotbar (1 × 9)                          │
└─────────────────────────────────────────────────────────┘
```

The GUI frame carries the tier accent color: Basic gray, Advanced orange (bronze), Elite cyan-blue, Ultimate purple.

## 4. Data Synchronization in Menus

- Slots synchronize item stacks.
- `DataSlot` carries only 16 bits; 32-bit values (energy, capacities) use the `core` helper that splits a value across two `DataSlot`s.
- Non-numeric data (fluid type) uses a dedicated packet sent only on change (planning topic 11).

## 5. Information Display

- Energy: FE with SI prefixes (`1.2 kFE`, `3.4 MFE`); fluids: mB or buckets.
- Tooltips: one short line; `Shift` shows tier values, usage and capacity.
- All text through translation keys.

## 6. In-World Interaction

See ADR 0046 for the full table: right-click opens GUIs; the wrench toggles faces and, with sneak, dismantles; item storage units support front-face insert/extract and display their contents; fluid tanks render their fluid level.
