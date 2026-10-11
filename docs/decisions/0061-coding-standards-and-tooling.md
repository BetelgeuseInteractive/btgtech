# 0061. Coding standards and tooling

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Code style must be enforced automatically. Latest versions on Maven Central (2026-10-11): JUnit 6.1.3, ArchUnit 1.5.1, Spotless Gradle plugin 8.10.4, palantir-java-format 2.102.0.

## Options Considered
- Formatter: **palantir-java-format** (4-space indent, 120 columns) versus google-java-format (2-space indent).
- Additional static analysis (Error Prone, SpotBugs) versus none in v1: noise and compatibility risk with Java 25 and Minecraft.
- Coverage measurement versus a unit-test rule for `logic` packages.

## Decision
- **Formatting:** Spotless with palantir-java-format; `gradlew build` fails on unformatted code; `spotlessApply` fixes it.
- **No additional static analysis** and **no coverage tool** in v1; Spotless, ArchUnit, tests and IntelliJ inspections are sufficient.
- **Pinned versions:** JUnit 6.1.3, ArchUnit 1.5.1, Spotless 8.10.4, palantir-java-format 2.102.0. ArchUnit's compatibility with JUnit 6 is verified in Phase 0 (not verified on 2026-10-11); if it fails, the latest JUnit 5 release is used instead.
- The full standard is maintained in `docs/development/coding-standards.md`.

## Consequences
- Version updates follow ADR 0012: deliberate, verified by a successful build.
