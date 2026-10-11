# 0054. Packet architecture

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
NeoForge 26.1 defines custom packets as `CustomPacketPayload` with a `StreamCodec`, registered through `RegisterPayloadHandlersEvent` (`event.registrar("<protocol version>")`); client-side handlers are registered separately through `RegisterClientPayloadHandlersEvent`. Handlers run on the main thread by default.

## Options Considered
A central payload list in `core` (would make `core` depend on features, violating ADR 0023) versus per-feature registration with a shared protocol version.

## Decision
- **API:** NeoForge payloads; no extra library.
- **Packages:** shared helpers and the protocol version constant live in **`core.packet`** (`core.network` is reserved for transport networks, ADR 0031). Payload classes live in the owning feature's **`packet`** sub-package; client handlers live in the feature's `client` package.
- **Registration:** each feature registers its own payloads in its `<Feature>Feature` / `<Feature>ClientFeature` bootstrap (ADR 0022), using the shared protocol version.
- **Protocol version:** one version string for the whole mod, incremented whenever any payload changes; NeoForge rejects mismatched clients.
- **Handler thread:** the default main thread.

## Consequences
- `packet` joins `client` and `logic` as a standard feature sub-package.
