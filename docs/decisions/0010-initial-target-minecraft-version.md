# 0010. Initial target Minecraft version

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The owner wants to support Minecraft 26.x from the first release to the latest and asked the architect to choose the most widely used 26.x version as the starting point.

Data collected on 2026-10-11 (NeoForge Maven, Modrinth API):

| Minecraft | Latest NeoForge | Status | NeoForge mods on Modrinth |
|---|---|---|---|
| 26.1 | 26.1.0.19-beta | beta | 6,161 |
| 26.1.1 | 26.1.1.15-beta | beta | 5,892 |
| 26.1.2 | 26.1.2.116 | stable | 7,931 |
| 26.2 | 26.2.0.90 | stable | 6,638 |
| 26.3 | 26.3.0.71-beta | beta | 4,011 |

## Options Considered
- **A. 26.1.2:** stable, most widely supported, actively maintained.
- **B. 26.3:** newest, but NeoForge is in beta (breaking changes allowed) and fewer mods support it.
- **C. 26.2:** stable, but neither the most popular nor the newest.

## Decision
Option A. Development of v1 targets **Minecraft 26.1.2** with the latest stable **NeoForge 26.1.2.x** build. Within the 26.1.x line only 26.1.2 is supported; 26.1 and 26.1.1 are superseded hotfix releases (the official NeoForge MDK also uses the exact range `[26.1.2]`).

## Consequences
- Players on 26.1 or 26.1.1 must update to the 26.1.2 hotfix.
- Other 26.x versions are added through ports (ADR 0011) after the v1 core is complete (ADR 0006).
- The exact NeoForge build is pinned in the build configuration and updated deliberately, not automatically.
