# 0001. Single repository with planning docs in `docs/`

- **Status:** Accepted (amended by [0067](0067-repository-files.md))
- **Date:** 2026-10-11

## Context
Planning documents and mod source code must live under the same root folder (`C:\dev\BTGTech`), yet planning and development must stay clearly separated. Anyone working on the code must be able to reference the plan reliably.

## Options Considered
- **A. Single repository:** the root is both the Git root and the Gradle root; planning lives in `docs/`. Plan and code share one history and references between them are simple relative paths. Plan documents become public.
- **B. Two sibling repositories** (`planning/`, `btgtech/`): plans could stay private, but cross-repository references are fragile and two histories must be kept in sync.

## Decision
Option A. `C:\dev\BTGTech` is a single Git repository. All planning documents live under `docs/`; approved decisions live under `docs/decisions/`. The detailed folder layout is decided separately (planning topic 4).

## Consequences
- Planning documents are public together with the source code.
- No source code is written outside the scope of an approved work package; during planning only `docs/` is modified.
