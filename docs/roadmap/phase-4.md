# Phase 4: Logistics → `0.4.0`

Goal: complete item and fluid storage and transport.

## WP-401 Fluid tank block

- **Scope:** `fluid_tank` in four tiers; bucket interaction; in-world fluid level renderer (throttled updates); contents kept on break.
- **Acceptance:** game tests: fill, drain, contents preserved after breaking.
- **Depends on:** WP-201.
- **References:** ADR 0029, 0046, 0055.

## WP-402 Item pipe

- **Scope:** `item_pipe` in four tiers (Blockbench model); connection modes `insert` / `extract` / `disabled`, cycled with the wrench through its `core` hook (WP-103); round-robin item transfer strategy every 10 ticks; no filters.
- **Acceptance:** unit tests for the transfer strategy; game tests: chest → pipe → machine; extract mode pulls machine outputs.
- **Depends on:** WP-101, WP-103.
- **References:** ADR 0030, 0031, 0043.

## WP-403 Fluid pipe

- **Scope:** `fluid_pipe` in four tiers; same connection modes; mB-per-tick transfer strategy.
- **Acceptance:** game test: tank → pipe → geothermal generator.
- **Depends on:** WP-402.
- **References:** ADR 0030, 0031, 0043.

## WP-404 Storage unit type lock

- **Scope:** wrench toggles the item storage unit type lock; lock kept when broken; `0.4.0` release.
- **Acceptance:** game test: locked empty unit rejects other items.
- **Depends on:** WP-103, WP-107.
- **References:** ADR 0029.
