# 0003. Project identity and distribution

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Identifiers such as the mod ID are baked into saves, registries and resource paths and are expensive to change later.

## Options Considered
The owner confirmed the proposed identity directly; no competing alternatives were raised.

## Decision
- **Display name:** BTG Tech
- **Mod ID:** `btgtech`
- **Publisher:** Betelgeuse Interactive (the owner's independent studio; "BTG" abbreviates Betelgeuse). GitHub account: `BetelgeuseInteractive`.
- **Source code:** public.
- **Distribution channels:** CurseForge, Modrinth and GitHub Releases.
- **Mod loader:** NeoForge only. Forge may be reconsidered far in the future; Fabric is not planned.

Still open: Java package root (planning topic 4), license (planning topic 2).

## Consequences
- The mod ID `btgtech` is the namespace for every registry entry, resource and data path.
- Release automation (planning topic 14) targets all three channels.
