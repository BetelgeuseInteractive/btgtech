# 0072. Roadmap and work packages

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Development follows the phases of ADR 0005. Work must be split into packages that can be completed, verified (ADR 0062) and merged through one pull request (ADR 0063).

## Options Considered
Tracking work in documents versus GitHub Issues; one file per work package versus one file per phase.

## Decision
- **Documents:** `docs/roadmap/roadmap.md` holds phases, versions and the status table of all work packages; `docs/roadmap/phase-0.md` … `phase-5.md` describe each work package (goal, scope, deliverables, acceptance criteria, dependencies, references).
- **Size rule:** a work package fits one working session and one reviewable pull request; larger work is split.
- **Numbering:** phase × 100 (`WP-001…` for Phase 0, `WP-101…` for Phase 1, …); branch `wp/<number>-<short-name>`.
- **Tracking:** status lives in `roadmap.md` (Pending / In progress / Done). GitHub Issues and Milestones may be added later.
- **Phases and versions:** Phase 0 Foundation (no release); Phase 1 Vertical slice `0.1.0`; Phase 2 Energy `0.2.0`; Phase 3 Machines `0.3.0`; Phase 4 Logistics `0.4.0`; Phase 5 Integration and polish `1.0.0`.
- The work package list and the owner task list are those in the roadmap documents as of this ADR.

## Consequences
- Adding, removing or reordering work packages requires the owner's approval; scope changes beyond ADR 0006 additionally require an ADR.
