# 0063. Git workflow

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Work arrives in small work packages (ADR 0005); each Minecraft version has its own branch (ADR 0011). The owner must keep control over what enters the main line.

## Options Considered
The workflow below was proposed as one set and approved.

## Decision

| Topic | Rule |
|---|---|
| Main branch | `main` = current development target (Minecraft 26.1.2) |
| Port branches | `mc/<version>` (e.g. `mc/26.2`, `mc/26.3`), created when porting starts. If `main` later moves to a newer version, the old line branches off as `mc/<old version>` |
| Work package branches | `wp/<number>-<short-name>` (e.g. `wp/012-energy-cable-network`) |
| Integration | Every work package enters `main` through a pull request. CI must pass; **the owner approves the merge**. Squash merge: one commit per work package on `main` |
| Commit messages | Conventional Commits (`feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `build:`), English, no attribution lines (ADR 0004) |

## Consequences
- Nothing enters `main` without the owner's approval.
- The workflow is described step by step in `docs/development/git-workflow.md`.
