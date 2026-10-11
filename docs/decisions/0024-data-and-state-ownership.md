# 0024. Data and state ownership

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
NeoForge 26.1 offers several places to keep state: block entities (saved through `ValueInput` / `ValueOutput`), data components on item stacks, and data attachments on other holders. A single rule per kind of state avoids duplicated or conflicting storage.

## Options Considered
The ownership table below was proposed and approved as the single rule set; no competing scheme was proposed.

## Decision

| State | Owner |
|---|---|
| Block state (energy, inventory, progress, configuration) | The block entity, persisted with `ValueInput` / `ValueOutput`; `setChanged()` is called after every persistent change |
| Item-stack state (e.g. a battery item's charge) | Data components |
| Extra state on holders without an own block entity | Data attachments, only when no block entity of ours can own it |
| Cable and pipe networks | Runtime caches rebuilt from the world; persistence details decided in planning topics 6 and 7 |

The server is the single authority for all game state; the client only displays it. Synchronization rules are defined in planning topic 11.

## Consequences
- No state is duplicated between a block entity and an attachment.
- Client code never mutates game state directly; it sends requests to the server (planning topic 11).
