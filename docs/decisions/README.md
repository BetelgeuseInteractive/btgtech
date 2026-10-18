# Architecture Decision Records

This folder is the authoritative log of approved project decisions. Planning documents describe the design; ADRs record *why* each choice was made.

## Rules

- One decision per file: `NNNN-short-kebab-case-title.md`, numbered sequentially, never reused.
- Only decisions explicitly approved by the project owner are recorded here.
- ADRs are never deleted or rewritten. When a decision changes, a new ADR is written and the old one's status is set to `Superseded by NNNN`. When a new ADR only adds an exception or extension, the old one's status becomes `Accepted (amended by NNNN)`.
- Every ADR uses the template below.

## Template

```markdown
# NNNN. Title

- **Status:** Accepted | Accepted (amended by NNNN) | Superseded by NNNN
- **Date:** YYYY-MM-DD

## Context
Why a decision was needed.

## Options Considered
Short list of alternatives with key trade-offs.

## Decision
What was chosen, stated unambiguously.

## Consequences
What follows from the decision: obligations, limits, follow-up work.
```

## Index

| ADR | Title | Status |
|---|---|---|
| [0001](0001-single-repository-with-docs-folder.md) | Single repository with planning docs in `docs/` | Accepted (amended by 0067) |
| [0002](0002-language-policy.md) | Language policy | Accepted |
| [0003](0003-project-identity-and-distribution.md) | Project identity and distribution | Accepted |
| [0004](0004-no-tool-attribution-in-repository.md) | No tool attribution in the repository | Accepted |
| [0005](0005-vertical-slice-development-approach.md) | Vertical-slice development approach | Accepted |
| [0006](0006-v1-content-scope.md) | v1 content scope | Accepted (amended by 0040, 0053) |
| [0007](0007-universal-four-tier-progression.md) | Universal four-tier progression | Accepted |
| [0008](0008-small-custom-material-set.md) | Small custom material set | Accepted |
| [0009](0009-server-safe-code-from-day-one.md) | Server-safe code from day one | Accepted |
| [0010](0010-initial-target-minecraft-version.md) | Initial target Minecraft version | Accepted |
| [0011](0011-branch-per-minecraft-version.md) | One branch per Minecraft version | Accepted |
| [0012](0012-build-toolchain.md) | Build toolchain | Accepted |
| [0013](0013-licensing.md) | Licensing | Accepted |
| [0014](0014-primary-ide.md) | Primary IDE | Accepted |
| [0015](0015-standard-development-commands.md) | Standard development commands | Accepted |
| [0016](0016-java-package-root.md) | Java package root | Accepted |
| [0017](0017-single-gradle-project.md) | Single Gradle project | Accepted |
| [0018](0018-feature-based-package-layout.md) | Feature-based package layout | Accepted (amended by 0075) |
| [0019](0019-repository-root-layout.md) | Repository root layout | Accepted (amended by 0067) |
| [0020](0020-documentation-layout.md) | Documentation layout | Accepted |
| [0021](0021-per-feature-registration.md) | Per-feature registration | Accepted |
| [0022](0022-entry-points-and-event-wiring.md) | Entry points and event wiring | Accepted (amended by 0058) |
| [0023](0023-core-boundary-and-feature-dependencies.md) | Core boundary and feature dependencies | Accepted |
| [0024](0024-data-and-state-ownership.md) | Data and state ownership | Accepted |
| [0025](0025-configuration-policy.md) | Configuration policy | Accepted |
| [0026](0026-energy-model.md) | Energy model | Accepted |
| [0027](0027-soft-tier-bottleneck.md) | Soft tier bottleneck | Accepted |
| [0028](0028-graph-based-cable-networks.md) | Graph-based cable networks | Accepted |
| [0029](0029-item-and-fluid-storage-blocks.md) | Item and fluid storage blocks | Accepted |
| [0030](0030-instant-transfer-pipe-networks.md) | Instant-transfer pipe networks | Accepted |
| [0031](0031-shared-network-infrastructure.md) | Shared network infrastructure | Accepted |
| [0032](0032-machine-framework-in-core.md) | Machine framework in `core` | Accepted |
| [0033](0033-tier-names-and-representation.md) | Tier names and representation | Accepted |
| [0034](0034-hybrid-machine-framework.md) | Hybrid machine framework | Accepted |
| [0035](0035-common-machine-features.md) | Common machine features | Accepted (amended by 0041) |
| [0036](0036-recipe-types-and-format.md) | Recipe types and format | Accepted |
| [0037](0037-recipe-runtime-behavior.md) | Recipe runtime behavior | Accepted |
| [0038](0038-v1-materials.md) | v1 materials | Accepted |
| [0039](0039-v1-content-identifiers-and-behavior.md) | v1 content identifiers and behavior | Accepted |
| [0040](0040-tiered-crafting-components.md) | Tiered crafting components | Accepted |
| [0041](0041-generators-push-energy-to-neighbors.md) | Generators push energy to neighbors | Accepted |
| [0042](0042-multiblock-approach.md) | Multiblock approach | Accepted |
| [0043](0043-tier-scaling-and-balance-defaults.md) | Tier scaling and balance defaults | Accepted |
| [0044](0044-gui-technology.md) | GUI technology | Accepted (amended by 0073) |
| [0045](0045-gui-layout-tier-colors-and-information-display.md) | GUI layout, tier colors and information display | Accepted (amended by 0074) |
| [0046](0046-in-world-interaction-standards.md) | In-world interaction standards | Accepted |
| [0047](0047-art-technical-rules.md) | Art technical rules | Accepted |
| [0048](0048-generated-tier-texture-variants.md) | Generated tier texture variants | Accepted |
| [0049](0049-art-tools-and-style-selection.md) | Art tools and style selection | Accepted (amended by 0074) |
| [0050](0050-data-generation-scope-and-output-policy.md) | Data generation scope and output policy | Accepted (amended by 0060, 0074) |
| [0051](0051-languages-and-translation.md) | Languages and translation | Accepted |
| [0052](0052-loot-and-breaking-rules.md) | Loot and breaking rules | Accepted |
| [0053](0053-tags-world-generation-creative-tab-and-advancements.md) | Tags, world generation, creative tab and advancements | Accepted |
| [0054](0054-packet-architecture.md) | Packet architecture | Accepted |
| [0055](0055-synchronization-channels.md) | Synchronization channels | Accepted (amended by 0073) |
| [0056](0056-network-security-and-performance-rules.md) | Network security and performance rules | Accepted |
| [0057](0057-v1-mod-integrations.md) | v1 mod integrations | Accepted |
| [0058](0058-integration-technical-rules.md) | Integration technical rules | Accepted |
| [0059](0059-data-level-public-api.md) | Data-level public API | Accepted |
| [0060](0060-test-strategy.md) | Test strategy | Accepted |
| [0061](0061-coding-standards-and-tooling.md) | Coding standards and tooling | Accepted |
| [0062](0062-definition-of-done.md) | Definition of Done | Accepted |
| [0063](0063-git-workflow.md) | Git workflow | Accepted |
| [0064](0064-versioning.md) | Versioning | Accepted |
| [0065](0065-minecraft-line-support-policy.md) | Minecraft line support policy | Accepted |
| [0066](0066-ci-and-release-pipeline.md) | CI and release pipeline | Accepted |
| [0067](0067-repository-files.md) | Repository files | Accepted |
| [0068](0068-github-repository.md) | GitHub repository | Accepted |
| [0069](0069-closed-tier-set.md) | Closed tier set | Accepted |
| [0070](0070-dimension-agnostic-design.md) | Dimension-agnostic design | Accepted |
| [0071](0071-space-technology-extension-points.md) | Space technology extension points | Accepted |
| [0072](0072-roadmap-and-work-packages.md) | Roadmap and work packages | Accepted |
| [0073](0073-full-32-bit-menu-data-slots.md) | Full 32-bit menu data slots | Accepted |
| [0074](0074-art-style.md) | Art style | Accepted |
| [0075](0075-data-generation-may-use-client-classes.md) | Data generation may use client classes | Accepted |
