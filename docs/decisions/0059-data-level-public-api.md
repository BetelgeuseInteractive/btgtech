# 0059. Data-level public API

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The owner is open to offering an API for other mods. A code API is a long-term compatibility promise; the architecture has not yet been validated by real use.

## Options Considered
- **A. No code API in v1; a documented, stable data-level API** (capabilities, tags, recipe formats, data maps, registry IDs).
- **B. A small code API** (`api` package with tiers, recipe type IDs, constants) published as a separate jar; requires Maven publishing (Maven Central needs the domain, ADR 0016) and a version compatibility promise.
- **C. A full add-on API** (other mods adding machines or network types): premature.

## Decision
Option A. v1 publishes no Java API. The supported integration surface is documented in `docs/architecture/compatibility.md` and kept stable:
- NeoForge capabilities exposed by BTG blocks (energy, items, fluids),
- `c:` material tags and BTG tags,
- BTG recipe types and their JSON format,
- the data map `btgtech:geothermal_fuels`,
- registry IDs (ADR 0039).

## Consequences
- Breaking any item above requires a new ADR and a changelog entry.
- A code API (option B or C) is reconsidered in v1.x when there is concrete demand; space technology development is expected to clarify the need.
