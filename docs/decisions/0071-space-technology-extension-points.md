# 0071. Space technology extension points

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Space technology is out of v1 scope (ADR 0006); the architecture must support it without empty skeleton code. NeoForge has no official API for creating dimensions at runtime; the library mod Infiniverse provides one for 26.1, other approaches rely on reflection and break across updates.

## Options Considered
Writing placeholder code now versus documenting extension points only; deciding the space-station approach now versus at the start of space work.

## Decision
Extension points are **documented only**; no code is written for them in v1:

| Extension point | Location | Design |
|---|---|---|
| Multiblock controller (ADR 0042) | `core.machine` | A controller block entity owns energy, item and fluid components; the structure pattern is data-defined (JSON); validation runs only when a member block changes; member blocks forward capabilities to the controller |
| Environment factors | `energy` (solar generation), later `space` | One designated place in the generation formula where an environment factor is applied; not present in v1 |
| Cross-dimension transport | `core.network` | A new endpoint type (ADR 0031); the graph is unchanged |
| `space` feature package | `space` | Created when space work is approved (ADR 0018); may depend on `core`, `energy` and `logistics` |
| Code API | `api` | Reconsidered if space work creates a concrete need (ADR 0059) |

**Deferred:** how space stations are implemented — own runtime dimensions, a library such as Infiniverse, or regions in one shared orbit dimension — is the **first ADR of the space phase**. Current lean: shared orbit dimension (lowest port risk).

## Consequences
- The design is kept in `docs/architecture/space-readiness.md`.
