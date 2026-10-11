# 0005. Vertical-slice development approach

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The owner is a solo developer with basic modding experience. Architecture, art style and workflow must be validated early, and work must be broken into small, verifiable packages.

## Options Considered
- **A. Vertical slice first:** after the foundation, build one minimal end-to-end slice through every layer, then expand each system. Early validation and an early playable build; some early restructuring is possible.
- **B. Layer by layer:** finish energy completely, then machines, then storage. Focused, but integration problems surface late and nothing is playable for a long time.
- **C. Whole v1 as a single target:** not controllable and cannot be split into verifiable steps.

## Decision
Option A. Development follows these phases (detailed work packages are defined in planning topic 16):

| Phase | Goal |
|---|---|
| 0. Foundation | Development environment, project skeleton, build, CI, datagen and localization pipeline, art-style demos |
| 1. Vertical slice | One generator, cable, battery, one machine and one storage block, each in all four tiers, end-to-end: GUI, synchronization, datagen, localization |
| 2. Energy | Complete the v1 energy content |
| 3. Machines | Complete the v1 processing machines |
| 4. Logistics | Complete item/fluid storage and transport |
| 5. Integration and polish | Mod compatibility, balancing, dedicated-server verification; **v1.0 release** |
| After v1.0 | Ports to other Minecraft versions, multiblocks, space technology |

## Consequences
- Every system must be usable in a minimal form before it is broadened.
- The vertical slice is the first real test of the core architecture; findings may produce follow-up ADRs.
