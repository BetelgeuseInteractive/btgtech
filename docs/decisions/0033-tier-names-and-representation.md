# 0033. Tier names and representation

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Every tiered block exists in four tiers (ADR 0007). Tier names and the technical representation of tiers had to be fixed before any content is registered.

## Options Considered
- Names: **A.** Basic / Advanced / Elite / Ultimate; **B.** Tier I–IV / Mk1–Mk4; **C.** material-based; **D.** star-themed (Dwarf / Giant / Supergiant / Hypergiant).
- Representation: **A.** a separate block per tier; **B.** one block with a `tier` block-state property; **C.** one block upgraded in place.

## Decision
- **Tier names:** Basic, Advanced, Elite, Ultimate (in this order). Display names are localized; identifiers always use the English lowercase form: `basic`, `advanced`, `elite`, `ultimate`.
- **Representation:** every tier is a **separate block** with its own item. A higher tier is crafted from the previous tier plus materials.
- **Registry ID pattern:** `<tier>_<block>`, e.g. `basic_crusher`, `elite_energy_cable`.
- The tier definition (`Tier` with the four values above) lives in `core` (ADR 0023).

## Consequences
- v1 registers four blocks per tiered content type.
- An in-world tier upgrade kit is a candidate for a later version and needs its own ADR.
