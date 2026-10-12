# 0074. Art style

- **Status:** Accepted
- **Date:** 2026-10-12

## Context
ADR 0049 required a style demo before any production texture: three candidates (V1 Vanilla+, V2 Clean industrial, V3 Understated sci-fi) compared in game next to vanilla blocks, fixing the tier color codes and the GUI frame style (ADR 0045).

WP-005 showed the three candidates in game. The owner rejected all three: they were not symmetric and did not look like they belonged in Minecraft. The owner asked for designs that are as symmetric as possible, fit the Minecraft look in our own style, and, for ores and ingots, keep the original vanilla structure with our own colors.

A second round showed two machine variants that follow vanilla machine conventions (A: dark stone and iron, like the blast furnace; B: iron frame and copper panel, like the crafter and copper blocks), each in the four tiers, and tin material forms recolored at runtime from the vanilla iron textures. Both rounds ran through a local resource pack on vanilla carrier blocks, without mod code.

## Options Considered
- **Machines:** variant A (stone and iron) or variant B (iron and copper).
- **Material forms with a vanilla counterpart:** runtime recoloring of the vanilla texture through the vanilla `paletted_permutations` atlas source (no Mojang pixels in the mod), shipping recolored copies of vanilla PNGs (derived from Mojang's textures and in conflict with the asset license of ADR 0013), or original drawings in vanilla style.
- **GUI frame:** tier-colored bevels, or the vanilla frame with tier-colored corner pieces.

## Decision
1. **Style:** BTG textures follow the conventions of vanilla machine blocks with our own designs: as symmetric as possible, no dark outline around faces, light from the top left, vanilla material ramps with subtle tone variation.
2. **Machines:** variant B. An iron frame with a copper panel, in the family of the crafter and the copper blocks. The function is shown in the center of the front.
3. **Tier colors** (ramp dark / mid / light), fixing the exact codes for ADR 0045:

   | Tier | Dark | Mid | Light |
   |---|---|---|---|
   | Basic (iron gray) | `#4F555B` | `#7A828A` | `#A9B0B7` |
   | Advanced (orange, bronze) | `#8C4A1E` | `#C9772F` | `#EDAA5F` |
   | Elite (cyan-blue) | `#1D6272` | `#2EA2B7` | `#86E2EE` |
   | Ultimate (purple) | `#4F2672` | `#8B48BA` | `#C69EEA` |

4. **Tier marking on blocks:** colored corner pieces in the four corners of machine faces. This replaces the "tier stripe" wording of ADR 0045; tier variants are still produced from a base texture and a tier mask (ADR 0048).
5. **GUI frame:** the vanilla panel and frame, with L-shaped tier-colored corner pieces in the inner corners.
6. **Material forms with a vanilla counterpart** (ore, deepslate ore, raw item, raw block, ingot, nugget, storage block) are recolored at runtime from the corresponding vanilla iron texture with a `paletted_permutations` atlas source. The mod ships only palette strips (vanilla color values and our colors). Forms without a vanilla counterpart (dust, plate) are drawn in vanilla style.
7. **Energy cable:** a copper conductor (4×4 arm) with an iron node (6×6×6) that shows the tier color.
8. **Mojang textures are never copied, traced or redistributed;** vanilla visuals are used only through model references or runtime recoloring.
9. The detailed rules (palettes, light direction, shading, tier-mask rules, file naming) live in `docs/development/art-style-guide.md`.

## Consequences
- ADR 0045 is amended: the exact tier colors are fixed here; blocks carry tier corner pieces instead of a tier stripe, and the GUI frame carries tier corner pieces.
- ADR 0049 is amended: the style demo is complete and its result is this ADR.
- ADR 0050 is amended: palette strips are hand-made resources; the atlas source definitions for runtime recoloring are produced by data generation (`SpriteSourceProvider`).
- Resource packs that replace the vanilla iron textures with different colors also change the look of our recolored forms; resource packs can override our forms directly.
- Production textures may now be made (ADR 0049); every texture is reviewed and refined by the owner.
