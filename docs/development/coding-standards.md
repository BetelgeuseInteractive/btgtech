# Coding Standards

Rules every contribution follows. Decisions: ADR [0002](../decisions/0002-language-policy.md), [0018](../decisions/0018-feature-based-package-layout.md), [0061](../decisions/0061-coding-standards-and-tooling.md).

## 1. Formatting

- Spotless with palantir-java-format (4-space indent, 120 columns). Run `.\gradlew.bat spotlessApply` before committing; `build` fails on unformatted code.
- Files are UTF-8.

## 2. Naming

| Element | Convention | Example |
|---|---|---|
| Packages | lowercase, singular, one English word where possible | `com.betelgeuseinteractive.btgtech.energy.logic` |
| Classes | PascalCase nouns | `EnergyNetworkGraph` |
| Feature bootstrap / holders | `<Feature>Feature`, `<Feature>ClientFeature`, `<Feature><RegistryType>` | `EnergyFeature`, `EnergyBlocks` |
| Payloads | `<Name>Payload` | `TankFluidPayload` |
| Methods, fields, locals | camelCase verbs/nouns | `distributeEnergy` |
| Constants | UPPER_SNAKE_CASE | `PROTOCOL_VERSION` |
| Registry IDs, resource paths | snake_case; tiered: `<tier>_<block>` | `elite_energy_cable` |
| Translation keys | ADR 0051 patterns | `block.btgtech.basic_crusher` |

- No abbreviations except established ones (FE, GUI, ID, mB).
- Placeholder or meaningless names are forbidden (`test1`, `temp`, `stuff`, `misc`, `utils2`, `NewFolder`, …).

## 3. Structure

- Code lives in its feature package; shared code in `core` only with two users (ADR 0023).
- Client-only code only in `client` packages; Minecraft-independent logic only in `logic` packages; payloads in `packet` packages; integrations in `compat.<modid>`.
- Inheritance from `MachineBlockEntity` at most two levels deep (ADR 0034).
- Event listeners are registered explicitly in bootstrap classes; annotation discovery only in `compat` where a mod API requires it (ADR 0022, 0058).

## 4. Logging and Errors

- One logger per class: `private static final Logger LOGGER = LogUtils.getLogger();`.
- `System.out` / `printStackTrace` are forbidden.
- Never swallow exceptions silently; log with context.

## 5. Comments and Documentation

- English. Comments explain *why*, not *what*.
- Javadoc is required on public classes and methods in `core`.
- No commented-out code; no TODO without an owner and a reason.

## 6. Performance Rules

- No per-tick work without need: idle machines sleep (ADR 0035), synchronization only on change (ADR 0056).
- Cache capability lookups with `BlockCapabilityCache` (ADR 0026).
- No allocation-heavy work in tick methods.
