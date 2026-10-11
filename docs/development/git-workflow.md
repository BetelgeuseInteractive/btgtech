# Git Workflow

How changes move from a work package into a release. Decisions: ADR [0011](../decisions/0011-branch-per-minecraft-version.md), [0063](../decisions/0063-git-workflow.md), [0064](../decisions/0064-versioning.md), [0065](../decisions/0065-minecraft-line-support-policy.md).

## 1. Branches

| Branch | Purpose |
|---|---|
| `main` | Current development target (Minecraft 26.1.2) |
| `mc/<version>` | Supported port line, e.g. `mc/26.2` |
| `wp/<number>-<short-name>` | One work package |

## 2. Work Package Flow

1. Start from an up-to-date `main`: create `wp/<number>-<short-name>`.
2. Commit in small steps using Conventional Commits (`feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `build:`), in English, without attribution lines.
3. Satisfy the [Definition of Done](definition-of-done.md).
4. Open a pull request into `main` summarizing what changed and how it was verified.
5. CI must pass.
6. The owner approves; the pull request is squash-merged; the branch is deleted.

## 3. Versions and Tags

- Version format: `<major>.<minor>.<patch>+mc<minecraft version>`, e.g. `0.1.0+mc26.1.2`.
- Major = breaks worlds or configs; minor = new content; patch = fixes. Before v1: `0.x`, one minor per phase.
- Tag format: `v<version>`, e.g. `v0.1.0+mc26.1.2`.

## 4. Release Train (multiple Minecraft lines)

1. A minor release is finished on `main`.
2. Its changes are ported in one batch to each supported `mc/<version>` branch, each through a pull request with passing CI.
3. Every line is tagged with the same mod version and its own `+mc` suffix.
4. Patch fixes are ported only to the lines that have the bug.
