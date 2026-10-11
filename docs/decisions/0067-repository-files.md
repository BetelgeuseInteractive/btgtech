# 0067. Repository files

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
ADR 0019 fixed the repository root layout but did not list Git, GitHub and contributor files. Commits already showed LF/CRLF line-ending warnings.

## Options Considered
The file set below was proposed and approved, including adding `.gitattributes` during planning as an exception to ADR 0001.

## Decision
Added to the root layout of ADR 0019:

| Path | Content |
|---|---|
| `.gitignore` | Official MDK ignores plus IDE folders |
| `.gitattributes` | LF line endings (`* text=auto eol=lf`), CRLF for `gradlew.bat`, `*.png` as binary |
| `.github/workflows/` | CI and release workflows (ADR 0066) |
| `.github/ISSUE_TEMPLATE/` | Bug report template asking for Minecraft, NeoForge and mod versions and the log file |
| `CHANGELOG.md` | Release notes, "Keep a Changelog" format |
| `CONTRIBUTING.md` | Contribution rules: code MPL-2.0, assets All Rights Reserved (ADR 0013); translation pull requests (ADR 0051); Conventional Commits (ADR 0063) |

**Exception to ADR 0001:** `.gitattributes` is added during planning so line endings are normalized before any code exists.

## Consequences
- ADR 0019 is amended by this list.
