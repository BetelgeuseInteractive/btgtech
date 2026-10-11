# 0006. v1 content scope

- **Status:** Accepted (amended by [0040](0040-tiered-crafting-components.md), [0053](0053-tags-world-generation-creative-tab-and-advancements.md))
- **Date:** 2026-10-11

## Context
Because every block has four tiers (ADR 0007), each new content type multiplies blocks, textures and recipes. The target difficulty is "in the middle": neither as deep as GregTech nor simplistic. Without a fixed scope v1 cannot be finished.

## Options Considered
- **S (core, ~30 blocks):** 2 generators, battery, cable, 3 machines, storage unit, tank, no pipes.
- **M (~55 blocks):** listed below.
- **L (~90+ blocks):** M plus more generators, autocrafter, miner, networked storage, one multiblock.

## Decision
Package M. v1.0 contains:

| Area | Content |
|---|---|
| Energy generation | Solid fuel generator, solar panel, geothermal (lava) generator |
| Energy storage and transfer | Battery block, energy cable |
| Processing machines | Electric furnace, crusher, compressor, alloy smelter, fluid filler/drainer |
| Item and fluid storage | Single-type high-capacity item storage unit, fluid tank |
| Transport | Item pipe, fluid pipe |
| Tools | Wrench |
| Materials | Small custom material set (ADR 0008) |

Names above are working descriptions; final English names and exact machine behavior are fixed in planning topic 8.

**Out of scope for v1.0:**
- Space technology (only extension points are prepared)
- Multiblock structures (candidate for the first post-v1 feature)
- Wireless energy
- Networked storage (AE2-style)
- Autocrafter, miner/quarry
- Armor, jetpacks and tools other than the wrench
- In-game guidebook
- Forge and Fabric support
- Ports to other Minecraft versions before the v1 core is complete (strategy decided in planning topic 2)

## Consequences
- Any content outside this list requires a new, approved ADR before implementation.
- Architecture must still leave room for multiblocks and space technology without implementing them.
