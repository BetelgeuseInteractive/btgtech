# 0013. Licensing

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The owner wants the mod to be open source "with some restrictions". Large mods commonly license code and assets separately (e.g. Create: code MIT, assets All Rights Reserved; AE2 and Iris: code LGPL-3.0; Mekanism: MIT; Sodium: PolyForm Shield, source-available).

## Options Considered
- **A. Code MIT, assets ARR:** code fully permissive, including closed-source reuse.
- **B. Code LGPL-3.0, assets ARR:** modified code must stay open; "linking" is ambiguous for Java mods.
- **C. Code MPL-2.0, assets ARR:** file-level copyleft; modified BTG Tech files must stay open, while other mods and add-ons keep their own licenses; simpler compliance than LGPL.
- **D. PolyForm Shield / Noncommercial:** strongest restrictions, but not open source and discourages contributors.

## Decision
Option C:
- **Code:** Mozilla Public License 2.0 (MPL-2.0). Applies to all source code, build scripts and data files that are not assets.
- **Assets:** All Rights Reserved, © Betelgeuse Interactive. Applies to textures, models, sounds and other art under the mod's `assets` resources.
- **Explicit permissions granted with the asset license:**
  1. The unmodified mod may be included in modpacks.
  2. Resource packs may retexture the mod's content.

## Consequences
- Because the jar contains ARR assets, third parties may not re-upload the mod elsewhere without permission; forks must supply their own assets.
- License files and per-file headers (if any) are defined in planning topics 4 and 13.
- Contributions are accepted under these licenses (contribution terms defined in planning topic 14).
