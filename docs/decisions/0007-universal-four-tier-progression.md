# 0007. Universal four-tier progression

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The owner requires every machine to have four tiers. It had to be decided whether this also applies to generators, storage and transport blocks.

## Options Considered
- **A. All tiered content has four tiers:** consistent progression and one shared tier model.
- **B. Only processing machines have tiers:** fewer blocks, but an inconsistent progression model.

## Decision
Option A. Every v1 block that can meaningfully scale exists in exactly four tiers: generators, batteries, energy cables, processing machines, item storage units, fluid tanks, item pipes and fluid pipes. The tier determines the relevant capability, for example processing speed, generation rate, capacity or transfer rate.

Tier names, stat values and whether tiers are separate blocks or an upgradeable property are decided in planning topic 8.

## Consequences
- A single shared tier definition is part of the core architecture; per-block ad-hoc tier logic is not allowed.
- Art, recipes, loot and localization must be produced for four variants of each tiered block, so the art style must support cheap tier variation.
