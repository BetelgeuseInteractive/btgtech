# 0020. Documentation layout

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Planning will produce many documents. Each document must have a single responsibility and a predictable location.

## Options Considered
A flat `docs/` folder becomes hard to navigate as documents grow; a small set of purpose-based sub-folders was proposed and approved.

## Decision

| Path | Content |
|---|---|
| `docs/README.md` | Index of all documents |
| `docs/planning-status.md` | Planning progress and pending owner requirements |
| `docs/decisions/` | Architecture Decision Records |
| `docs/development/` | How to work on the project: `environment-setup.md`, later `coding-standards.md`, `git-workflow.md`, `release-process.md` |
| `docs/architecture/` | `overview.md` plus one document per subsystem (core, energy, logistics, machines, gui, data-generation, networking, compatibility, space-readiness) |
| `docs/roadmap/` | Phases and work packages (planning topic 16) |

## Consequences
- `docs/environment-setup.md` moves to `docs/development/environment-setup.md`.
- Each document is created only when its planning topic is approved; no empty placeholder documents.
