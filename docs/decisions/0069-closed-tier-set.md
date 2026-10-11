# 0069. Closed tier set

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Space technology will extend progression after v1. A fifth machine tier would touch every machine, the config, the color system, data generation and all textures.

## Options Considered
- **(a)** Keep four tiers; space progression uses new materials and new blocks (e.g. rocket levels).
- **(b)** Add a fifth tier later (e.g. "Stellar").

## Decision
Option (a). The tier set Basic / Advanced / Elite / Ultimate (ADR 0033) is **closed**. Later progression, including space technology, is expressed through new materials, components and content, not through additional machine tiers.

## Consequences
- Code may treat the four tiers as a fixed set.
