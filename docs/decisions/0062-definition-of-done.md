# 0062. Definition of Done

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Work is delivered in small work packages (ADR 0005). A shared, checkable definition of "done" prevents half-finished work from accumulating.

## Options Considered
The criteria below were proposed as one set and approved.

## Decision
A work package is done only when all of the following hold:
1. `gradlew build` succeeds (compilation, formatting, unit tests, architecture tests, data checks).
2. `runData` has been run and its output is committed.
3. If behavior changed: `runGameTestServer` succeeds and new behavior has a game test.
4. All new translation keys exist in all eight languages.
5. If something visual changed: checked with `runClient` and reported to the owner.
6. If the design changed: the affected documents are updated; if a decision changed: a new ADR is written with the owner's approval.
7. No placeholder names, unused files or unowned TODOs.

## Consequences
- The checklist is maintained in `docs/development/definition-of-done.md`.
