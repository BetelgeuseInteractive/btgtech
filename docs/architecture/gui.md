# GUI and Interaction

Standards for screens, information display and in-world interaction. Decisions: ADR [0044](../decisions/0044-gui-technology.md), [0045](../decisions/0045-gui-layout-tier-colors-and-information-display.md), [0046](../decisions/0046-in-world-interaction-standards.md), [0073](../decisions/0073-full-32-bit-menu-data-slots.md). Visual style and art production: ADR [0047](../decisions/0047-art-technical-rules.md)–[0049](../decisions/0049-art-tools-and-style-selection.md).

## 1. Technology

- Vanilla container screens; no GUI library.
- 26.1 API: screens submit render state via `GuiGraphicsExtractor` / `extractRenderState`; GUI textures are sprites under `assets/btgtech/textures/gui/sprites/`, drawn with `blitSprite` (tiling and nine-slice available).
- Prefer sprites and vanilla widgets over custom drawing; every custom drawing routine is a port risk.

## 2. Widget Set (`core.machine.client`)

| Widget | Class | Purpose |
|---|---|---|
| Screen base | `MachineScreen` | Tier panel, slot frames, energy bar, redstone mode button, title (section 3) |
| Energy bar | `EnergyBar` | Stored / capacity, filled from the bottom; tooltip with SI amounts, exact values with `Shift` |
| Fluid gauge | (WP-201) | Fluid type, amount / capacity |
| Progress arrow | `ProgressArrow` | Processing progress, filled from left to right |
| Redstone mode button | `RedstoneModeButton` | Vanilla button with the mode icon; cycles ignore / with signal / without signal through the menu button of `MachineMenu`; tooltip and narration name the mode |
| Machine mode button | (with `fluid_processor`) | Machine-specific modes (e.g. `fluid_processor` fill/drain) |
| Tooltip helpers | `MachineTooltips` | Short line + `Shift` details; SI formatting |

Minecraft-independent parts live in `core.machine.logic` and are unit-tested: `QuantityFormat` (SI and exact amounts) and `GaugeScale` (filled length of a gauge). Translation keys are constants in `MachineTranslationKeys`, shared with the language provider.

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

The GUI uses the vanilla panel and frame with tier-colored corner pieces in the inner corners: Basic gray, Advanced orange (bronze), Elite cyan-blue, Ultimate purple (ADR [0074](../decisions/0074-art-style.md); exact colors and shapes in the [art style guide](../development/art-style-guide.md)).

Positions in pixels from the top left corner of the 176 × 166 panel:

| Element | Position | Size |
|---|---|---|
| Title | centered at y 6; moved left when it would reach the redstone mode button, shortened with an ellipsis only if it does not fit between x 8 and 150 | |
| Redstone mode button | 152, 6 | 16 × 16 |
| Energy bar (frame) | 7, 16 | 14 × 54 |
| Player inventory label | 8, 72 | |
| Player inventory and hotbar slots | 8, 84 and 8, 142 (vanilla positions) | |

- Machine slots and gauges keep clear of the energy bar (x 7–20, on machines with energy), the redstone mode button (x 152–167, y 6–21) and the tier corner pieces (the 11 × 11 px in each corner).
- `MachineScreen` draws the vanilla slot sprite (`minecraft:container/slot`) behind every slot of the menu, so the panel itself carries no slots and serves every machine.
- The panels are the tier variants `btgtech:machine/<tier>_panel`, generated from one base texture and a tier mask (ADR [0048](../decisions/0048-generated-tier-texture-variants.md)).

## 4. Data Synchronization in Menus

- Slots synchronize item stacks.
- A `DataSlot` carries a full 32-bit `int` under NeoForge (ADR [0073](../decisions/0073-full-32-bit-menu-data-slots.md)); energy amounts and capacities use one `DataSlot` each. Confirmed in game in WP-105: the battery screen shows 123,456 FE exactly.
- Non-numeric data (fluid type) uses a dedicated packet sent only on change (planning topic 11).

## 5. Information Display

- Energy: FE with SI prefixes (`1.2 kFE`, `3.4 MFE`); fluids: mB or buckets.
- SI amounts keep one decimal digit and are rounded down, so a value just below a limit never shows the limit (`999,999` → `999.9 kFE`); amounts below 1,000 have no prefix and no decimal digit. Exact amounts group thousands with commas (`12,345 FE`).
- Like vanilla, numbers use the same separators in every language; the unit and its position come from the translation (`gui.btgtech.energy`, `.kilo`, `.mega`, `.giga`).
- Tooltips: one short line; `Shift` shows tier values, usage and capacity. Without `Shift`, a dark gray hint line says that details exist.
- All text through translation keys.

## 6. In-World Interaction

See ADR 0046 for the full table: right-click opens GUIs; the wrench toggles faces and, with sneak, dismantles; item storage units support front-face insert/extract and display their contents; fluid tanks render their fluid level.
