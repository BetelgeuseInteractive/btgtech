# 0004. No tool attribution in the repository

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Some development tools add attribution lines or promotional text to commits, pull requests and generated files by default.

## Options Considered
The owner set this as a firm rule; no alternatives were considered.

## Decision
No tool attribution appears anywhere in the repository or its GitHub presence unless the owner explicitly states otherwise. This includes, but is not limited to:
- `Co-Authored-By:` trailers naming a tool or service in commit messages,
- "Generated with …" or similar lines in pull request descriptions, release notes or issues,
- tool credits or mentions in README, documentation, changelogs or source files.

## Consequences
- Any tool default that adds attribution must be disabled before the tool is used on this repository.
- Commit and pull request content is reviewed for such lines before publishing.
