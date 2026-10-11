# 0057. v1 mod integrations

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Availability on NeoForge (Modrinth, 2026-10-11): JEI and Jade support 26.1.2 and 26.3 and are the most downloaded mods of their kind (JEI 82 M, Jade 71 M). REI, WTHIT and The One Probe support 26.1.2. EMI, Mekanism and Create have no 26.x NeoForge release (latest 1.21.1). AE2 supports 26.1.2 (beta) and 26.3.

## Options Considered
Integrating every available recipe viewer and overlay mod versus integrating the dominant one of each kind and relying on NeoForge capabilities for everything else.

## Decision

| Mod | v1 |
|---|---|
| **JEI** | Integrated: categories for BTG recipe types, machines as catalysts, recipe transfer into machine GUIs |
| **Jade** | Integrated: energy, processing progress, tank fluid and storage unit contents of the targeted BTG block |
| REI, WTHIT, The One Probe | Later candidates |
| EMI, Mekanism, Create | None (no 26.x release) |
| AE2 and other storage/transport mods | No dedicated code; interoperate through NeoForge capabilities (ADR 0026, 0029) |
| Patchouli | None (guidebook out of v1 scope, ADR 0006) |

## Consequences
- When large mods reach 26.x, capability-based interoperability works without changes.
- Each additional integration needs its own ADR.
