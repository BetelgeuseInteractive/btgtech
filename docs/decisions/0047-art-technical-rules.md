# 0047. Art technical rules

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
v1 contains about 55 blocks plus items. The owner requires a vanilla-compatible style with modern, minimalist touches, consistent across all content.

## Options Considered
The rules below were proposed as one set and approved; the main alternative for resolution (32×32) was rejected for vanilla compatibility and solo production speed.

## Decision

| Topic | Rule |
|---|---|
| Resolution | 16×16 textures |
| Items | 2D sprites |
| Blocks | Cube models; cables, pipes and the solar panel use Blockbench models |
| Active state | Machine fronts show an animated "active" texture (`.mcmeta` animation, `active` block-state property) while working |
| Active-state stability | Once changed, `active` stays unchanged for at least 20 ticks, preventing flicker and repeated chunk re-renders on short pauses |
| Sounds | Vanilla sounds only in v1; custom sounds are a later candidate |

## Consequences
- Every machine-like block has a front texture in an idle and an active variant.
