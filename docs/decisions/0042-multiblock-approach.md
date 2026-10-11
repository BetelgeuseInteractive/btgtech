# 0042. Multiblock approach

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The owner asked to discuss multiblock structures. ADR 0006 places them outside v1 scope as a candidate for the first post-v1 feature.

## Options Considered
Including a multiblock in v1 (package L) versus deferring multiblocks while preparing an extension point.

## Decision
- No multiblock structures in v1 and no multiblock code is written for v1.
- First candidates after v1: large battery, boiler and turbine, later a space launch pad.
- The extension point (a controller block entity plus structure validation in `core.machine`) is specified in planning topic 15.

## Consequences
- The machine framework (ADR 0034) must not make assumptions that prevent a controller block entity from owning components later.
