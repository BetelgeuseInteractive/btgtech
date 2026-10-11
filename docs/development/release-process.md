# Release Process

How a version is released. Decisions: ADR [0064](../decisions/0064-versioning.md), [0065](../decisions/0065-minecraft-line-support-policy.md), [0066](../decisions/0066-ci-and-release-pipeline.md).

## 1. Channels

| Version range | GitHub Releases | Modrinth | CurseForge |
|---|---|---|---|
| `0.x` | Pre-release | — | — |
| `1.0.0` and later | Release | Release | Release |

## 2. Owner Prerequisites (once, before v1.0)

1. Create the BTG Tech project on Modrinth and on CurseForge.
2. Create API tokens on both platforms and store them as GitHub Secrets `MODRINTH_TOKEN` and `CURSEFORGE_TOKEN`.
3. Provide the project IDs so they can be added to the release workflow.

## 3. Steps for a Release

1. All work packages of the release are merged into `main`; CI is green.
2. `CHANGELOG.md`: move entries from "Unreleased" to a new version section with the date.
3. Set `mod_version` in `gradle.properties`.
4. For a minor release: port to every supported `mc/<version>` line (release train, ADR 0065).
5. The owner approves the release.
6. Tag each line: `v<version>+mc<minecraft version>`, e.g. `v0.1.0+mc26.1.2`, and push the tags.
7. The release workflow builds the jar and publishes it with mc-publish; verify each channel.

## 4. Before v1.0 Only Once

- Dedicated-server verification (ADR 0009) and balance tuning (ADR 0043) are complete.
- The repository is public and `main` is protected (ADR 0068).
