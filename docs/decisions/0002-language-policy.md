# 0002. Language policy

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The project owner communicates in Turkish, while code, identifiers and the project's reference material benefit from a single professional language.

## Options Considered
- **A. English documents, Turkish conversation:** consistent with code; unambiguous reference material; accessible to future contributors.
- **B. Turkish documents:** easier for the owner to read, but mixes languages with code and identifiers.

## Decision
Option A:
- All planning documents, ADRs, code comments, commit messages and contributor guidelines are written in English.
- All folder, file, package, class and identifier names are professional English. Placeholder names (`test1`, `temp`, `stuff`, `misc`, `utils2`, …) are forbidden.
- Conversation with the project owner (questions, reports, approvals) is in Turkish.
- The mod's default in-game language is English (`en_us`). 5–8 of the world's most widely used languages are supported from the start; the exact list is decided in planning topic 10.

## Consequences
- Reports and questions addressed to the owner are in Turkish; every repository artifact is in English.
