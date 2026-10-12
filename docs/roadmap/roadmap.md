# Roadmap

Phases, versions and work package status. Decisions: ADR [0005](../decisions/0005-vertical-slice-development-approach.md), [0072](../decisions/0072-roadmap-and-work-packages.md). Every work package must satisfy the [Definition of Done](../development/definition-of-done.md) and follows the [Git workflow](../development/git-workflow.md).

## 1. Phases

| Phase | Goal | Version | Details |
|---|---|---|---|
| 0 | Foundation | — | [phase-0.md](phase-0.md) |
| 1 | Vertical slice | `0.1.0` (GitHub pre-release) | [phase-1.md](phase-1.md) |
| 2 | Energy | `0.2.0` | [phase-2.md](phase-2.md) |
| 3 | Machines | `0.3.0` | [phase-3.md](phase-3.md) |
| 4 | Logistics | `0.4.0` | [phase-4.md](phase-4.md) |
| 5 | Integration and polish | `1.0.0` | [phase-5.md](phase-5.md) |
| After v1 | Release-train ports, multiblocks, space phase planning | — | — |

## 2. Work Package Status

| WP | Title | Status |
|---|---|---|
| WP-001 | Project bootstrap | In progress |
| WP-002 | Quality tooling | Pending |
| WP-003 | Continuous integration | Pending |
| WP-004 | Core infrastructure | Pending |
| WP-005 | Art style demo | Pending |
| WP-101 | Network infrastructure | Pending |
| WP-102 | Machine framework | Pending |
| WP-103 | Wrench | Pending |
| WP-104 | Materials part 1 | Pending |
| WP-105 | Energy slice | Pending |
| WP-106 | Crusher and recipe base | Pending |
| WP-107 | Item storage unit | Pending |
| WP-108 | Slice game tests, translations and 0.1.0 | Pending |
| WP-201 | Fluid tank component | Pending |
| WP-202 | Solar panel | Pending |
| WP-203 | Geothermal generator | Pending |
| WP-204 | Generator push and balance config | Pending |
| WP-301 | Materials part 2 | Pending |
| WP-302 | Electric furnace | Pending |
| WP-303 | Compressor | Pending |
| WP-304 | Alloy smelter | Pending |
| WP-305 | Fluid processor | Pending |
| WP-401 | Fluid tank block | Pending |
| WP-402 | Item pipe | Pending |
| WP-403 | Fluid pipe | Pending |
| WP-404 | Storage unit type lock | Pending |
| WP-501 | JEI integration | Pending |
| WP-502 | Jade integration | Pending |
| WP-503 | Advancements | Pending |
| WP-504 | Translation review | Pending |
| WP-505 | Balance testing | Pending |
| WP-506 | Dedicated server verification | Pending |
| WP-507 | Release 1.0.0 | Pending |

## 3. Owner Tasks

| When | Task |
|---|---|
| Before WP-001 | Acquire the domain `betelgeuseinteractive.com` (ADR 0016); install IntelliJ IDEA (ADR 0014) |
| First `runServer` | Accept the Minecraft EULA |
| WP-005 | Choose the art style |
| End of every work package | Approve the pull request merge |
| End of Phase 0 | Make the repository public; enable `main` protection (ADR 0068) |
| WP-504 | Review the Turkish translation |
| Before WP-507 | Create Modrinth and CurseForge projects; store `MODRINTH_TOKEN` and `CURSEFORGE_TOKEN` as GitHub Secrets |
