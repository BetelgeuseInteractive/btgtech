# Art Style Guide

Rules for every BTG Tech texture and model. Decisions: ADR [0047](../decisions/0047-art-technical-rules.md), [0048](../decisions/0048-generated-tier-texture-variants.md), [0049](../decisions/0049-art-tools-and-style-selection.md), [0074](../decisions/0074-art-style.md).

## 1. Principles

- BTG textures look like vanilla blocks and items, with our own designs. Reference families: the crafter and the copper blocks for machines; vanilla ores and metals for materials.
- As symmetric as possible: fronts and sides mirror left to right; tops mirror in both axes. Tone variation may break symmetry at single-pixel level only.
- No dark outline around a block face; edges are shaped by lighter and darker tones of the material.
- 16×16 textures (ADR 0047). Every texture is reviewed and refined by the owner (ADR 0049).
- Mojang textures are never copied, traced or redistributed. Vanilla visuals are used only through model references or runtime recoloring (section 5).

## 2. Light and Shading

- Light comes from the top left: top and left edges are lighter, bottom and right edges darker.
- Recesses (windows, vents) are darkest just below their top edge, where the frame casts a shadow.
- Metal surfaces get subtle tone variation: about 10–20 % of the pixels take a neighboring tone of the same ramp, in short horizontal runs. Tier corner pieces and function icons stay clean.
- Rendering layer follows the texture's alpha (Minecraft 26.1): fully opaque faces are solid, pixels with alpha 0 are cut out, partial alpha is translucent. Use partial alpha only where translucency is intended.

## 3. Machine Palette

| Material | Tones (dark → light) |
|---|---|
| Iron frame | `#4E4E4E` `#747474` `#9C9C9C` `#BEBEBE` `#DEDEDE` |
| Copper panel | `#7A3E28` `#9A5038` `#B26247` `#C87456` `#E3826C` |
| Recess | `#141414` `#242424` |

Machine faces: a beveled iron frame (lightest tone on the top row and left column), a copper panel or copper-framed window in the center, and the function shown inside the window (for example two crushing rollers on the crusher front). Sides and tops repeat the frame and the copper panel without a window.

## 4. Tiers

| Tier | Dark | Mid | Light |
|---|---|---|---|
| Basic (iron gray) | `#4F555B` | `#7A828A` | `#A9B0B7` |
| Advanced (orange, bronze) | `#8C4A1E` | `#C9772F` | `#EDAA5F` |
| Elite (cyan-blue) | `#1D6272` | `#2EA2B7` | `#86E2EE` |
| Ultimate (purple) | `#4F2672` | `#8B48BA` | `#C69EEA` |

- **Blocks:** 2×2 corner pieces, one pixel in from each edge (pixels 1–2 and 13–14), on every machine face. Upper pieces are lit (light tone at the top left), lower pieces are in shadow (mid and dark tones).
- **Base texture and mask (ADR 0048):** the base texture draws the corner pieces in the Basic ramp; the tier mask is a grayscale image of the same size that is white exactly on the corner pieces and black elsewhere. Data generation replaces the Basic tones under the mask with the matching tones of the target tier.
- **Cables:** the tier color sits in the center of the node faces (section 7).
- **GUI:** see section 8.

## 5. Materials

- Forms with a vanilla counterpart (ore, deepslate ore, raw item, raw block, ingot, nugget, storage block) are recolored at runtime from the vanilla iron texture of the same form with a `minecraft:paletted_permutations` atlas source. The mod ships two palette strips per form: the vanilla colors (key) and the material colors. Atlas source definitions are produced by data generation.
- For ores, only the ore colors are in the key; the stone or deepslate colors stay unchanged.
- Forms without a vanilla counterpart (dust, plate) are drawn once in vanilla item style as a gray template (`item/dust`, `item/plate`) and recolored at runtime the same way, with one palette strip per material; every material's dust and plate share one shape.
- Recolored sprites are named after their source: `minecraft:<vanilla texture>_btgtech_<material>` (the `_btgtech_` separator keeps them apart from sprites other mods derive from the same vanilla texture) and `btgtech:<template>_<material>`.
- A palette key holds each color of its texture once, darkest first; a material palette has the same length.
- Each material gets its own palette, reviewed by the owner.

**Tin:** a clearly blue, light silver.
- Metal forms and templates: each tone is replaced by its luminance multiplied by (0.86, 0.96, 1.10) for red, green and blue, capped at 255.
- Ore clusters, darkest to lightest: `#465868` `#6A8399` `#97B1C6` `#C2D7E6` `#E6F1F8`.

**Bronze:** a warm brown-orange, darker and browner than copper, less yellow than gold.
- Metal forms and templates: each tone is replaced by its luminance multiplied by (0.95, 0.66, 0.36).

**Templates:** gray tones `#3A3A3A` `#5C5C5C` `#808080` `#A6A6A6` `#CACACA` `#EAEAEA`, light from the top left.

## 6. Crafting Components

- Machine casing: a cube with the machine side design on all faces (iron frame, copper panel) and tier corner pieces in all four corners, as a base texture and tier mask (section 4).
- Circuit: a green board with copper contacts and a chip; the label on the chip carries the tier tones, as a base texture and tier mask.

## 7. Energy Cable

- Model: an arm of 4×4 px across the block and a 6×6×6 px node in the center (Blockbench model, ADR 0047).
- Texture layout (16×16): rows 0–3 arm side (16×4), rows 4–7 columns 0–3 arm end (4×4), rows 8–13 columns 0–5 node face (6×6).
- Copper conductor (copper panel ramp, section 3) with a short repeating pattern along the arm; iron node with the tier color as a 2×2 center.

## 8. GUI

- Vanilla panel and frame: panel `#C6C6C6`, 2-px bevels `#FFFFFF` (top left) and `#555555` (bottom right), 1-px `#000000` outline with rounded corners.
- Tier corner pieces in the four inner corners: L-shapes with 8-px arms, 2 px thick, starting 3 px in from the outer edge. Outer row and column in the mid tone, the corner pixels in the light tone, the inner row and column in the dark tone.
- The panel is one base texture with the corner pieces in the Basic ramp and a tier mask, like block textures (section 4); its four corners mirror each other.
- Slots use the vanilla slot sprite; buttons use the vanilla button sprites (referenced, never copied).
- Gauges sit in a recess like a slot: `#373737` top and left edge, `#FFFFFF` bottom and right edge, `#8B8B8B` inside.
- Energy fill in redstone red, light from the left (`#5A0808` `#7A0E0E` `#A51616` `#D42020` `#E8453A` `#FF7A66`), with one darker row every 4 px counted from the bottom.
- Progress arrow: empty in `#8B8B8B`, filled in `#FFFFFF`, symmetric about its middle row.
- Fuel flame (14 × 14 px): a symmetric flame, empty in `#8B8B8B`, burning with a `#C23A12` edge, `#FF8A1F` body and `#FFD84A` core; it shrinks from the top as the fuel is used up.
- Icons (10 × 10 px) are drawn in the colors of what they show and stay readable on the vanilla button: redstone dust for "ignore", a lit and an unlit redstone torch for the two signal modes.
- Layout and widgets: [gui.md](../architecture/gui.md).

## 9. File Naming

| File | Location |
|---|---|
| Block base texture | `assets/btgtech/textures/block/<block>_<face>.png`, faces `front`, `front_active` (animated: frames stacked vertically, with `.mcmeta`), `side`, `top`, `bottom` (optional; the top serves as the bottom when there is none) |
| Tier mask | `assets/btgtech/textures/block/<block>_<face>_tier_mask.png` |
| Generated tier texture | `assets/btgtech/textures/block/<tier>_<block>_<face>.png` (in `src/generated/resources`) |
| Block base texture with one texture on all faces | `assets/btgtech/textures/block/<block>.png`, tier mask `<block>_tier_mask.png`, generated `<tier>_<block>.png` |
| Item texture | `assets/btgtech/textures/item/<item>.png` |
| Item base texture and tier mask | `assets/btgtech/textures/item/<item>.png` and `<item>_tier_mask.png`, generated `<tier>_<item>.png` |
| Material template | `assets/btgtech/textures/item/<form>.png` (`dust`, `plate`) |
| GUI sprite | `assets/btgtech/textures/gui/sprites/machine/<sprite>.png`, sprite ID `btgtech:machine/<sprite>` |
| GUI base texture and tier mask | `assets/btgtech/textures/gui/sprites/machine/<sprite>.png` and `<sprite>_tier_mask.png` |
| Generated GUI tier sprite | `assets/btgtech/textures/gui/sprites/machine/<tier>_<sprite>.png` (in `src/generated/resources`) |
| Palette strips | `assets/btgtech/textures/palettes/<vanilla texture or template>_key.png` and `<vanilla texture or template>_<material>.png` |
| Art sources | One Aseprite file per exported texture or mask, named like it: `art/block/<texture>.aseprite`, `art/item/<texture>.aseprite`, `art/gui/machine/<sprite>.aseprite`; Blockbench models `art/model/<model>.bbmodel` (ADR 0019) |

All names are lowercase snake_case and use the registry IDs of ADR 0039.
