# 0034. Hybrid machine framework

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
About 40 of the v1 blocks are machines, generators or batteries sharing energy, inventories, progress and redstone handling. The framework structure determines how cheaply new machines (including future space technology) can be added.

## Options Considered
- **A. Deep inheritance** (`BaseMachine` → `ProcessingMachine` → `Crusher` …): becomes rigid and fragile.
- **B. Fully component-based generic machine:** flexible but abstract and hard to read.
- **C. Hybrid:** one abstract `MachineBlockEntity` with pluggable components.

## Decision
Option C:
- One abstract `MachineBlockEntity` in `core.machine`. Inheritance depth from it is **at most two levels**.
- Behavior is composed from components: energy, item slots, fluid tanks, processing progress, redstone control.
- Each machine is described by a **machine definition** data object: slot layout, energy usage, recipe type and the components it uses. Numeric values are read per tier from the `SERVER` config (ADR 0025).
- A machine gets its own small subclass only when it needs behavior that components cannot express (e.g. generators).

## Consequences
- Adding a machine is mostly writing a definition plus data (recipes, models, translations).
- Components are generic and live in `core.machine`; machine-specific logic stays in the owning feature.
