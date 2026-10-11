# 0015. Standard development commands

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
A fixed set of commands with a clear purpose makes verification repeatable. Run names follow the official NeoForge 26.1.2 MDK.

## Options Considered
The official MDK run set was adopted as is; the only open choice was the Gradle daemon heap (MDK default `-Xmx1G` vs. a larger value on the 32 GB development machine).

## Decision

| Command | Purpose | When |
|---|---|---|
| `gradlew build` | Compile and run unit tests | After every change |
| `gradlew runData` | Run data generation into `src/generated/resources` | Whenever generated content changes |
| `gradlew runGameTestServer` | Run automated in-game tests on a headless server | When a work package is completed |
| `gradlew runClient` | Launch the game for manual testing | When visual or interactive verification is needed |
| `gradlew runServer` | Launch a dedicated server | Mandatory before v1.0 (ADR 0009) |

The Gradle daemon heap is set to `-Xmx4G` (`org.gradle.jvmargs`).

## Consequences
- The first `runServer` requires accepting the Minecraft EULA in the server run directory's `eula.txt`; this is done by the owner, never automatically.
- Detailed setup and troubleshooting steps live in the environment setup document (planning topic 3).
