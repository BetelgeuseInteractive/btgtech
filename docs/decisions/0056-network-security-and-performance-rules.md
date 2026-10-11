# 0056. Network security and performance rules

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
In May 2026 NeoForge fixed an exploit where a client could crash a server by sending a huge collection size ("Mitigating Network Vulnerabilities"; fixed in 26.1.2.44, covered by our target build). NeoForge recommends bounding every decoded collection, sending only player intent, and validating everything on the server.

## Options Considered
The rules below were proposed as one set and approved.

## Decision
**Security**
1. Serverbound packets carry **intent only** (button id, mode index); no packet sets server state directly.
2. The server validates every request: the player's relevant menu is open and valid (`stillValid`), and every value is within its allowed range.
3. Every decoded collection and string has an explicit upper bound; a client-provided size is never used directly for allocation.
4. Invalid packets are ignored and logged; they never crash the server.

**Performance**
1. Synchronize only on change; unconditional per-tick packets are forbidden.
2. At most one block entity update packet per block every 10 ticks.
3. Packets go only to the relevant players (those tracking the chunk or with the menu open).

## Consequences
- Serverbound payload handlers must include validation code.
