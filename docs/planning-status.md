# Planning Status

Tracks the architecture planning sessions. Approved results are recorded as ADRs in [decisions/](decisions/README.md); this file only tracks progress and pending inputs.

## Topics

| # | Topic | Status | ADRs |
|---|---|---|---|
| 0 | Planning document location | Done | 0001–0004 |
| 1 | Goals and scope | Done | 0005–0009 |
| 2 | Technical foundation (Minecraft/NeoForge/Java versions, build, dependencies, license, multi-version strategy) | Done | 0010–0013 |
| 3 | Development environment | Done | 0014–0015 |
| 4 | Project and folder structure | Done | 0016–0020 |
| 5 | Core architecture | Done | 0021–0025 |
| 6 | Energy system | Done | 0026–0028 |
| 7 | Item/fluid storage and transport | Done | 0029–0031 |
| 8 | Machine and automation framework | Done | 0032–0043 |
| 9 | GUI/UX | Done | 0044–0049 |
| 10 | Data and content generation | Done | 0050–0053 |
| 11 | Networking and synchronization | Done | 0054–0056 |
| 12 | Compatibility and public API | Done | 0057–0059 |
| 13 | Testing and quality | Done | 0060–0062 |
| 14 | Version control, release and CI/CD | Done | 0063–0068 |
| 15 | Space technology preparation | Done | 0069–0071 |
| 16 | Phased roadmap and work packages | Next | |

## Owner Requirements Awaiting a Decision

Stated by the owner at kickoff (2026-10-11); each is turned into an ADR in the listed topic.

| Requirement | Topic |
|---|---|
| Acquire the domain `betelgeuseinteractive.com` before the Phase 0 bootstrap (ADR 0016) | 16 |
| Art-style demo as a Phase 0 work package (ADR 0049) | 16 |
| Make the repository public at the end of Phase 0 or the first alpha (ADR 0068) | 16 |

## Environment Snapshot (2026-10-11)

Windows 11 Pro for Workstations; Ryzen 5 5600 (6C/12T); 32 GB RAM; RTX 3060 Ti; 533 GB free on C:. Installed: Zulu JDK 25.0.4.1 (`JAVA_HOME`), JDK 26.0.2.1, JRE 8, Git 2.55.0, GitHub CLI 2.96.0 (authenticated as `BetelgeuseInteractive`), Blockbench 5.2.1. Not installed: global Gradle (not needed; the wrapper is used), IntelliJ IDEA, VS Code.
