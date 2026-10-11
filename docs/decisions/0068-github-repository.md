# 0068. GitHub repository

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The source code will be public (ADR 0003). Planning documents had no off-machine backup. On the GitHub Free plan, branch protection is available only for public repositories.

## Options Considered
- **A.** Create the repository now as private, push the planning documents (immediate backup), make it public when Phase 0 is finished or at the first alpha.
- **B.** Create it public now.
- **C.** Create it in Phase 0.

## Decision
Option A:
- Repository: **`BetelgeuseInteractive/btgtech`**, private until Phase 0 is finished or the first alpha, whichever comes first; then public.
- Before the first push, local history is squashed into one clean commit.
- When the repository becomes public, `main` is protected: pull request and passing CI required.

## Consequences
- Making the repository public is an owner action at the end of Phase 0 (roadmap, planning topic 16).
