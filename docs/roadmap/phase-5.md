# Phase 5: Integration and Polish → `1.0.0`

Goal: integrations, guidance, balance and release readiness.

## WP-501 JEI integration

- **Scope:** `compat.jei`: categories for all BTG recipe types, machines as catalysts, recipe transfer; optional dependency with a version range.
- **Depends on:** WP-305.
- **References:** ADR 0057, 0058.

## WP-502 Jade integration

- **Scope:** `compat.jade`: energy, progress, tank fluid, storage unit contents.
- **Depends on:** WP-404.
- **References:** ADR 0057, 0058.

## WP-503 Advancements

- **Scope:** 8–10 progression advancements via data generation, translated.
- **Depends on:** WP-404.
- **References:** ADR 0053.

## WP-504 Translation review

- **Scope:** owner reviews `tr_tr`; final completeness pass for all eight languages.
- **Depends on:** WP-503.
- **References:** ADR 0051.

## WP-505 Balance testing

- **Scope:** play-test progression; propose value changes to the owner; record approved changes in the balance table change log.
- **Depends on:** WP-503.
- **References:** ADR 0043.

## WP-506 Dedicated server verification

- **Scope:** run every feature on a dedicated server with a client; fix side-safety issues.
- **Depends on:** WP-505.
- **References:** ADR 0009.

## WP-507 Release 1.0.0

- **Scope:** owner prerequisites of [release-process.md §2](../development/release-process.md#2-owner-prerequisites-once-before-v10); release `1.0.0+mc26.1.2` to GitHub, Modrinth and CurseForge.
- **Depends on:** WP-501, WP-502, WP-504, WP-506.
- **References:** ADR 0064, 0066.
