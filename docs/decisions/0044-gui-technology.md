# 0044. GUI technology

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Rendering is the area that changes most between 26.x versions (26.2: rendering overhaul; 26.3: client rewrite). In 26.1, screens submit render state through `GuiGraphicsExtractor` / `extractRenderState`, and GUI textures are sprites drawn from `textures/gui/sprites` with `blitSprite` (tiling and nine-slice supported). Menu `DataSlot`s transmit only 16 bits (short) over the network.

## Options Considered
- **A. Vanilla screens plus a small BTG widget set:** no dependency; ports together with vanilla.
- **B. Third-party GUI library:** ready-made widgets, but every port waits for the library; a major risk for multi-version support (ADR 0011).
- **C. Data-driven (JSON) GUI system:** flexible, but an unnecessarily large framework for v1.

## Decision
Option A:
- Machine screens extend vanilla container screens.
- A small widget set lives in `core.machine.client`: energy bar, fluid gauge, progress arrow, redstone mode button, machine mode button, tooltip helpers.
- Sprites and vanilla widgets are preferred over custom drawing code to keep port cost low.
- 32-bit values are synchronized by splitting them across two 16-bit `DataSlot`s, through a helper in `core`. Non-numeric data (e.g. the fluid in a tank) is sent by a dedicated packet only when it changes (details in planning topic 11).

## Consequences
- No GUI library dependency.
- Every screen-specific drawing routine is a port risk and must be justified.
