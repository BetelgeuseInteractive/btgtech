# 0023. Core boundary and feature dependencies

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Without a clear rule, shared packages grow into a dumping ground, and features become entangled in ways that make porting and extension (e.g. space technology) expensive.

## Options Considered
The rules below were proposed as a set and approved; the alternative was leaving placement to case-by-case judgment.

## Decision
1. Code enters `core` only if at least two features use it, or if it is inherently cross-cutting (tier definition, configuration, networking base). "Might be needed later" is not a reason.
2. `core` never depends on a feature package.
3. Features may depend on each other directly (e.g. `machine` → `energy`), but **cyclic dependencies between feature packages are forbidden**.
4. Where possible, features interact through NeoForge capabilities rather than each other's classes; e.g. a machine draws energy through NeoForge's common energy interface, not through BTG Tech energy classes.
5. Rules 2 and 3 are enforced by automated architecture tests (planning topic 13).

## Consequences
- Moving code into `core` requires showing its second user.
- Cross-feature calls favor capabilities, which also keeps BTG Tech content compatible with other mods.
