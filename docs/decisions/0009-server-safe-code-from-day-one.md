# 0009. Server-safe code from day one

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The owner does not require multiplayer support at the start. However, in NeoForge single-player also runs an integrated server, so code that ignores the client/server split breaks even in single-player, and retrofitting it later is expensive.

## Options Considered
- **A. Server-safe from day one, formal dedicated-server testing before v1.0:** near-zero extra cost; no later rewrite.
- **B. Ignore client/server separation until multiplayer is needed:** faster at first, but bugs appear in single-player and a later rewrite is costly.

## Decision
Option A:
- All code respects the logical and physical client/server separation from the first line.
- Client-only code is never referenced from common or server code paths.
- Verifying on a dedicated server is a mandatory release gate for v1.0, not for earlier phases.

## Consequences
- Networking and synchronization rules (planning topic 11) apply from Phase 1.
- CI should at least start a dedicated server once the project skeleton exists (planning topics 13–14).
