# Phase 0: Foundation

Goal: a buildable, tested, continuously integrated project skeleton and an approved art style. No release.

## WP-001 Project bootstrap

- **Goal:** the official template adapted to BTG Tech; the game starts with the mod loaded.
- **Scope:** follow [environment-setup.md §4](../development/environment-setup.md#4-project-bootstrap-phase-0); create `BTGTech` (`@Mod`) and `BTGTechClient` (`@Mod(dist = CLIENT)`) under `com.betelgeuseinteractive.btgtech` (feature bootstrap calls are added as features appear; `CoreFeature` arrives in WP-004); root files `LICENSE` (MPL-2.0 text), `LICENSE-ASSETS.md` (ARR + modpack and resource pack permissions, ADR 0013), `README.md`, `.gitignore` (MDK and IDE folders only), `CHANGELOG.md` (Keep a Changelog, "Unreleased" section), `CONTRIBUTING.md` (ADR 0067).
- **Out of scope:** any content (blocks, items).
- **Acceptance:** verification steps 1, 2 and 5 of environment-setup.md §5 succeed; BTG Tech appears in the mod list.
- **Depends on:** owner tasks "domain" and "IntelliJ IDEA".
- **References:** ADR 0010, 0012, 0013, 0016, 0019, 0022, 0067.

## WP-002 Quality tooling

- **Goal:** formatting, tests and architecture rules enforced by `gradlew build`.
- **Scope:** Spotless with palantir-java-format; JUnit and ArchUnit with the pinned versions (ADR 0061), verifying ArchUnit with JUnit 6 (fallback: latest JUnit 5, recorded in the PR); the architecture rules of [testing.md §2](../development/testing.md#2-required-architecture-rules); the translation completeness check.
- **Acceptance:** `build` fails on an unformatted file and on a deliberate rule violation (shown in the PR, then reverted).
- **Depends on:** WP-001.
- **References:** ADR 0060, 0061.

## WP-003 Continuous integration

- **Goal:** every push and pull request is checked automatically.
- **Scope:** `.github/workflows/ci.yml` per ADR 0066 (build, data generation freshness, game tests); `.github/ISSUE_TEMPLATE/` bug report. Determine whether the game test server needs the Minecraft EULA in CI; if yes, stop and ask the owner.
- **Acceptance:** CI is green on the pull request.
- **Depends on:** WP-002.
- **References:** ADR 0066, 0067.

## WP-004 Core infrastructure

- **Goal:** shared infrastructure every feature needs.
- **Scope:** `Tier` (`basic`, `advanced`, `elite`, `ultimate`); `SERVER` and `CLIENT` config registration; `core.packet` protocol version and the 32-bit `DataSlot` split helper; the "BTG Tech" creative tab; data generation wiring (`GatherDataEvent.Client`, language provider); a proof that data generation can write PNG files (ADR 0048), reported in the PR.
- **Acceptance:** unit tests for the `DataSlot` helper; `runData` produces output; PNG proof result documented.
- **Depends on:** WP-001.
- **References:** ADR 0025, 0033, 0044, 0048, 0050, 0054.

## WP-005 Art style demo

- **Goal:** the owner chooses the visual style.
- **Scope:** per ADR 0049: three styles (V1 Vanilla+, V2 Clean industrial, V3 Understated sci-fi) for Basic crusher (front, side), Basic energy cable, tin ore and tin ingot; in-game screenshots next to vanilla blocks; tier accent color proposals; GUI frame proposal. Demo files are not production assets.
- **Acceptance:** the owner's choice is recorded in a new ADR and in `docs/development/art-style-guide.md` (palette, light direction, shading, tier-mask rules, file naming).
- **Depends on:** WP-004.
- **References:** ADR 0045, 0047, 0048, 0049.

## Milestone: End of Phase 0

Owner makes the repository public and enables `main` protection (ADR 0068).
