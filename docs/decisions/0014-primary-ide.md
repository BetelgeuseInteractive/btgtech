# 0014. Primary IDE

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The project needs a reference IDE for browsing code, debugging and launching the game. ModDevGradle documents IntelliJ IDEA integration only.

## Options Considered
- **A. IntelliJ IDEA (free tier):** first-class ModDevGradle support with generated run configurations; strongest Java debugger. Since 2025.3 a single product whose core features are free, including for commercial use.
- **B. VS Code with Java extensions:** lightweight, but ModDevGradle support is undocumented (not verified).
- **C. No IDE, terminal only:** no setup, but debugging and code navigation become difficult.

## Decision
Option A. IntelliJ IDEA (free tier) is the reference IDE. Every build, test and run task must also work from the terminal through the Gradle Wrapper, so no workflow depends on the IDE.

## Consequences
- The owner installs IntelliJ IDEA manually (it requires accepting a license agreement).
- Run configurations come from ModDevGradle; they are not maintained by hand.
