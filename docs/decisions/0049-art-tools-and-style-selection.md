# 0049. Art tools and style selection

- **Status:** Accepted (amended by [0074](0074-art-style.md))
- **Date:** 2026-10-11

## Context
The owner wants the visual style chosen through small demo tests, using Blockbench and a pixel-art editor.

## Options Considered
Pixel editors: Aseprite (industry standard, one-time purchase), Pixelorama (free, open source), Blockbench painting only (limited). Production: textures drafted under a fixed palette and refined by the owner, optionally supported by concept images.

## Decision
- **Tools:** Aseprite for pixel art, Blockbench for models; each is used where it fits best. Source files (`.aseprite`, `.bbmodel`) live in `art/` (ADR 0019); exported PNGs go to `src/main/resources/assets/btgtech/textures/`.
- **Production:** textures are drafted strictly within the project palette and are always reviewed and refined by the owner. Concept images may be used for inspiration only.
- **Style demo (Phase 0 work package):**
  1. Three style candidates: **V1 "Vanilla+"** (very close to vanilla iron and copper); **V2 "Clean industrial"** (flat metal panels, thin lines, a small status light); **V3 "Understated sci-fi"** (darker body, distinct glowing accents, bridging to future space technology).
  2. Three sample objects: Basic crusher (front and side), Basic energy cable, tin ore and tin ingot.
  3. Compared through in-game screenshots next to vanilla blocks. The owner chooses one style or a blend.
  4. The same demo fixes the exact tier color codes and the GUI frame style.
  5. The result is recorded in a new ADR and in `docs/development/art-style-guide.md` (palette, light direction, shading rules, tier-mask rules, file naming).

## Consequences
- No production textures are made before the style ADR is approved.
