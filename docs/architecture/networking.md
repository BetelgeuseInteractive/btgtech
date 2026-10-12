# Networking and Synchronization

How client and server exchange data. Decisions: ADR [0054](../decisions/0054-packet-architecture.md), [0055](../decisions/0055-synchronization-channels.md), [0056](../decisions/0056-network-security-and-performance-rules.md), [0073](../decisions/0073-full-32-bit-menu-data-slots.md); state ownership: ADR [0024](../decisions/0024-data-and-state-ownership.md).

## 1. Principles

- The server is the single authority; the client displays state and sends intent.
- Each kind of state has exactly one synchronization channel.
- Synchronize only on change, only to the players who need it.

## 2. Packet Layout

| Element | Location |
|---|---|
| Protocol version constant, shared codec helpers (e.g. bounded collections) | `core.packet` |
| Payload records (`CustomPacketPayload` + `StreamCodec`) | `<feature>.packet` |
| Clientbound handlers | `<feature>.client` |
| Registration | `<Feature>Feature` (common) and `<Feature>ClientFeature` (client handlers) |

Handlers run on the main thread. Increment the protocol version whenever any payload changes.

## 3. Channel Table

| Data | Channel | Frequency |
|---|---|---|
| GUI numbers | Menu `DataSlot`s (full 32-bit values, ADR 0073) | Vanilla, on change |
| GUI non-numeric data | BTG clientbound payload | On change, menu viewers only |
| World visual state | Block state | Vanilla |
| Block-entity render data | Block entity update packet | On visible change, ≤ 1 per block per 10 ticks |
| GUI buttons | Vanilla `clickMenuButton` | On click |
| Wrench / storage interactions | Server-side block interaction | On interaction |

## 4. Security Checklist for Serverbound Data

- [ ] Carries intent only (ids, indices), never state.
- [ ] Server checks the menu is open and `stillValid`.
- [ ] Every value range-checked; every collection and string bounded.
- [ ] Invalid input is ignored and logged, never crashes.

## 5. Performance Checklist

- [ ] No unconditional per-tick packets.
- [ ] Block entity updates throttled to one per 10 ticks per block and sent only when the displayed value changes.
- [ ] Recipients limited to chunk trackers or menu viewers.
