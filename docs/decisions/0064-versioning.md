# 0064. Versioning

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Players must see which mod version belongs to which Minecraft version, and when an update may affect their worlds.

## Options Considered
- **A. SemVer with Minecraft build metadata:** `1.0.0+mc26.1.2`.
- **B. Minecraft prefix:** `26.1.2-1.0.0`; common, but does not sort as SemVer.

## Decision
Option A:
- Format: `<major>.<minor>.<patch>+mc<minecraft version>`, e.g. `1.0.0+mc26.1.2`.
- **Major:** breaks world or config compatibility. **Minor:** new content. **Patch:** bug fixes.
- **Before v1:** `0.x`; the Phase 1 vertical slice is `0.1.0`, each later phase a new minor version.
- **World compatibility promise:** within one major version, existing worlds keep working; registry IDs are permanent (ADR 0039).
- Git tags: `v<version>`, e.g. `v1.0.0+mc26.1.2`.

## Consequences
- The same mod version exists once per supported Minecraft line, distinguished by the `+mc` suffix.
