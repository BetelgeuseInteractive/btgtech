# 0055. Synchronization channels

- **Status:** Accepted (amended by [0073](0073-full-32-bit-menu-data-slots.md))
- **Date:** 2026-10-11

## Context
Each kind of client-visible state needs exactly one synchronization mechanism to avoid duplicated traffic and inconsistent displays.

## Options Considered
The table below was proposed as one set and approved; the main alternative for GUI buttons, a custom serverbound packet, was rejected in favor of vanilla's menu button mechanism.

## Decision

| Data | Mechanism | When |
|---|---|---|
| Numbers in an open GUI (energy, progress) | Menu `DataSlot`s; 32-bit values split across two slots (ADR 0044) | Vanilla sends changed values automatically |
| Non-numeric data in an open GUI (fluid type, machine mode) | BTG clientbound payload | Only on change, only to players with the menu open |
| Visible world state (machine active, cable/pipe connections) | Block state | Vanilla synchronization |
| Block-entity render data (storage unit item and count, tank fluid level) | Block entity update packet | Only when the displayed value changes, at most once every 10 ticks per block |
| GUI buttons (redstone mode, machine mode) | Vanilla menu button click (`clickMenuButton`) | On click |
| Wrench and storage-unit interactions | Normal block interaction handled on the server | On interaction |

## Consequences
- v1 is expected to need only one or two custom payloads.
- Cable and pipe networks produce no packets (they have no block entities).
