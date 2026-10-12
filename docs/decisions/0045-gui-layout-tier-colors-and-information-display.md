# 0045. GUI layout, tier colors and information display

- **Status:** Accepted (amended by [0074](0074-art-style.md))
- **Date:** 2026-10-11

## Context
About 40 machine-like blocks have GUIs. A shared layout lets players learn one screen and know them all. Four tiers multiply art cost (ADR 0007), which a shared color system can reduce.

## Options Considered
Tier colors: **A.** gray / orange (bronze) / cyan-blue / purple; **B.** the same but Ultimate in Betelgeuse red. Layout and display rules were proposed as one standard.

## Decision
**Layout:** every machine screen uses the same skeleton:
- vanilla-sized panel, 176 px wide;
- player inventory at the bottom;
- energy bar always at the same position on the left;
- redstone mode button always at the top right;
- title centered.

**Tier colors (option A):** Basic = gray, Advanced = orange (bronze), Elite = cyan-blue, Ultimate = purple. The accent color is used for the GUI frame, the tier stripe on block textures and the item name. Exact color codes are fixed in the art-style demo (planning topic 9, art pipeline).

**Information display:**
- Energy in FE with SI prefixes (`1.2 kFE`, `3.4 MFE`); fluids in mB or buckets.
- Tooltips stay short; details (tier values, usage, capacity) appear while `Shift` is held.
- All text comes from translation keys; no hard-coded strings (planning topic 10).

## Consequences
- Tier variants of textures are produced from a base texture plus the tier accent color (method decided with the art pipeline).
