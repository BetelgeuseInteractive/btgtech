# 0066. CI and release pipeline

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Checks must run automatically for every change, and releases must reach GitHub, Modrinth and CurseForge reliably. On 2026-10-11, `Kira-NT/mc-publish` (v3.3.1, July 2026) publishes to all three channels in one step and is maintained; alternatives are Minotaur (Modrinth only) and CurseForgeGradle (no release since 2023). Current actions: `actions/checkout` v7, `actions/setup-java` v6, `gradle/actions` v6.

## Options Considered
One multi-channel publishing action versus one Gradle plugin per channel; publishing pre-v1 builds everywhere versus only as GitHub pre-releases.

## Decision
**CI** (GitHub Actions, Linux, Java 25, Gradle caching via `gradle/actions`), on every pull request and push:
1. `gradlew build`
2. `gradlew runData`, then fail if generated output differs from the committed output (ADR 0050)
3. `gradlew runGameTestServer` (ADR 0060)

If the game test server requires accepting the Minecraft EULA, that acceptance is added to CI only by the owner's explicit decision (not verified on 2026-10-11).

**Release** (on pushing a tag `v<version>`, ADR 0064):
- Build the jar and publish with **mc-publish**.
- **Before v1 (`0.x`):** GitHub Releases only, marked as pre-release.
- **From v1.0:** GitHub Releases, Modrinth and CurseForge.
- Release notes come from `CHANGELOG.md` ("Keep a Changelog" format); every work package adds its entry.
- `MODRINTH_TOKEN` and `CURSEFORGE_TOKEN` are GitHub Secrets entered by the owner only.

## Consequences
- The Modrinth and CurseForge projects are created by the owner before v1.0.
- The step-by-step procedure lives in `docs/development/release-process.md`.
