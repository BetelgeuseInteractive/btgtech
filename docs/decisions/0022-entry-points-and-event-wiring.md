# 0022. Entry points and event wiring

- **Status:** Accepted (amended by [0058](0058-integration-technical-rules.md))
- **Date:** 2026-10-11

## Context
NeoForge supports a common `@Mod` entry point and a client-only entry point (`@Mod(value = MODID, dist = Dist.CLIENT)`, as in the official 26.1.2 MDK). Event listeners can be registered explicitly (`IEventBus#addListener`) or discovered automatically (`@EventBusSubscriber`).

## Options Considered
- **A. Explicit registration** in each feature's bootstrap class: every subscription of a feature is visible in one file.
- **B. `@EventBusSubscriber` discovery:** less code, but subscriptions are scattered and invisible from the entry point.

## Decision
Option A, with two entry points:
- **`BTGTech`** (`@Mod`): calls `<Feature>Feature.register(IEventBus modBus)` for every feature, in one fixed, documented order.
- **`BTGTechClient`** (`@Mod(dist = Dist.CLIENT)`): calls `<Feature>ClientFeature.register(IEventBus modBus)` for every feature that has client code. Client bootstrap classes live in the feature's `client` package.
- All event listeners (mod bus and game bus) are registered with `addListener` inside these bootstrap classes. `@EventBusSubscriber` is not used.

## Consequences
- Adding a feature always means adding exactly one line to `BTGTech` (and, if needed, one to `BTGTechClient`).
- The bootstrap order is described in `docs/architecture/core.md`.
