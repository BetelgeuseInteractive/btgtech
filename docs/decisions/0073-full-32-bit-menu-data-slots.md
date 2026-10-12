# 0073. Full 32-bit menu data slots

- **Status:** Accepted
- **Date:** 2026-10-12

## Context
ADR 0044 and ADR 0055 assumed that menu `DataSlot`s transmit only 16 bits, as in vanilla, where `ClientboundContainerSetDataPacket` writes the value as a short, and required a `core` helper that splits 32-bit values across two slots.

During WP-004 the NeoForge 26.1.2.116 sources were checked: NeoForge patches the container listener of `ServerPlayer` (`broadcastDataValue`) to send `AdvancedContainerSetDataPayload`, which carries the full `int`, whenever the client has the NeoForge network channel. The vanilla short packet is used only for clients without NeoForge. BTG Tech must be installed on the client, so every client that can open a BTG menu has that channel.

## Options Considered
- **A. Rely on NeoForge's full-int synchronization:** no helper; one `DataSlot` per value.
- **B. Keep the split helper as planned:** works with vanilla clients too, but doubles the slots per value and adds code that has no effect under NeoForge.

## Decision
Option A:
- A menu `DataSlot` carries a full 32-bit `int`; energy amounts and capacities use one `DataSlot` each.
- No split helper is created. This replaces the split-helper statement of ADR 0044 and the "32-bit values split across two slots" note of ADR 0055.
- The behavior is confirmed in Phase 1 with the first GUI that shows a value above 32,767 (WP-105, `runClient`).

## Consequences
- ADR 0044 and ADR 0055 are amended by this ADR.
- WP-004 no longer contains the helper or its unit tests.
- If a future NeoForge version drops the payload, a new ADR reintroduces the split before that version is supported.
