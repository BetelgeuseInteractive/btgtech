# 0046. In-world interaction standards

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Consistent world interactions make the mod predictable. Advanced storage needs direct, visible interaction to feel advanced.

## Options Considered
The interaction table below was proposed as one standard and approved, including three additions: sneak-wrench dismantling, front-face storage unit interaction and display, and visible fluid level in tanks.

## Decision

| Action | Result |
|---|---|
| Right-click a machine-like block | Opens its GUI |
| Right-click a face with the wrench | Toggles that face open/closed (ADR 0035) |
| Sneak + right-click with the wrench | Dismantles the block into its item; drops are identical to normal breaking |
| Right-click the front of an item storage unit | Inserts the held stack; a double right-click inserts every matching item from the player's inventory |
| Left-click the front of an item storage unit | Extracts one stack; sneak + left-click extracts one item |
| Front of an item storage unit | Displays the stored item icon and count |
| Fluid tank | Fluid level is rendered in the world (transparent tank); right-click with a bucket or fluid container fills or drains |

## Consequences
- Left-clicking the front of an item storage unit must not start breaking the block; breaking from other faces works normally.
- The storage unit display and tank fluid rendering are client renderers in `storage.client`; they are port-sensitive and kept minimal.
