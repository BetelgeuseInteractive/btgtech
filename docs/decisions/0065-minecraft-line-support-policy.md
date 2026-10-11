# 0065. Minecraft line support policy

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The owner wants to support all Minecraft 26.x versions. ADR 0011 left the support policy per line open. Each additional line means porting every feature again.

## Options Considered
- **A. Every line gets every feature immediately:** full parity; cost = features × lines.
- **B. Release train:** features are developed on `main` and ported to all supported lines in one batch per minor release; patch fixes are ported as needed.
- **C. Only the newest line gets features:** cheapest, contradicts the owner's goal.

## Decision
Option B:
- New features are developed on `main`.
- At each **minor** release, the release's features are ported in one batch to every supported `mc/<version>` line, and the release is published for all of them.
- **Patch** releases are ported where the bug exists.
- Moving a line to "fixes only" (e.g. when a Minecraft version's API changes make porting excessive) requires the owner's approval and a new ADR.

## Consequences
- Porting effort is paid once per minor release, not once per feature.
- Releases list the Minecraft lines they were published for.
