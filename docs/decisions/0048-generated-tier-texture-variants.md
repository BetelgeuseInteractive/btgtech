# 0048. Generated tier texture variants

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Every tiered block needs four visual variants (ADR 0007), distinguished by the tier accent color (ADR 0045).

## Options Considered
- **A. Data generation writes one PNG per tier** by coloring a grayscale tier mask (stripe or frame) with the tier color and compositing it over the base texture: no runtime cost, resource packs can override each tier, one production pipeline.
- **B. Runtime tinting:** one texture, color applied at runtime; requires client code (port risk) and is hard for resource packs to change.
- **C. Hand-painting every tier:** four times the work.

## Decision
Option A. Artists produce one base texture and one grayscale tier mask per block; data generation produces the four tier textures.

## Consequences
- Writing PNG files from data generation on 26.1 is verified during Phase 1.
- Generated textures are subject to the same commit policy as other generated resources (planning topics 10 and 14).
