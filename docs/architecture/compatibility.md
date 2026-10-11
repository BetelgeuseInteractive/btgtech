# Compatibility and Integration

How BTG Tech works with other mods and what other mods and modpacks may rely on. Decisions: ADR [0057](../decisions/0057-v1-mod-integrations.md), [0058](../decisions/0058-integration-technical-rules.md), [0059](../decisions/0059-data-level-public-api.md).

## 1. Integrations

| Mod | Content | Package |
|---|---|---|
| JEI | Categories for `btgtech:crushing`, `compressing`, `alloying`, `fluid_filling`, `fluid_draining`; machines as catalysts (electric furnace for smelting); recipe transfer into machine GUIs | `compat.jei` |
| Jade | Energy stored, processing progress, tank fluid, storage unit contents | `compat.jade` |

Rules: optional dependencies with version ranges in `neoforge.mods.toml`; compile against APIs only; code loaded only when the mod is present; annotation discovery (`@JeiPlugin`, `@WailaPlugin`) allowed only in these packages.

## 2. Capability-Based Interoperability

Every BTG machine-like block exposes standard NeoForge capabilities on its open faces (ADR 0035), so any mod using them interoperates without dedicated code:

| Capability | Exposed by |
|---|---|
| `Capabilities.Energy.BLOCK` | Generators, batteries, processing machines |
| `Capabilities.Item.BLOCK` | Processing machines, solid fuel generator, item storage unit |
| `Capabilities.Fluid.BLOCK` | Geothermal generator, fluid processor, fluid tank |

## 3. Data-Level API (stable)

Modpacks and other mods may rely on the following; breaking changes require a new ADR and a changelog entry.

| Surface | Details |
|---|---|
| Registry IDs | All IDs in ADR 0039, ADR 0038 materials and ADR 0040 components, namespace `btgtech` |
| Tags | `c:` material tags (`c:ores/tin`, `c:ingots/bronze`, `c:dusts/lead`, `c:plates/steel`, …) |
| Recipe types | `btgtech:crushing`, `btgtech:compressing`, `btgtech:alloying`, `btgtech:fluid_filling`, `btgtech:fluid_draining`; fields per ADR 0036 (sized ingredients, outputs with optional chance, optional `processing_time`, optional `minimum_tier`). The exact JSON schema is documented here when Phase 1 implements it |
| Data maps | `btgtech:geothermal_fuels`: fluid → energy per 1,000 mB |
| Configuration | `SERVER` config balance values (ADR 0025) |

## 4. Not Provided in v1

- No Java API jar and no Maven artifact (ADR 0059).
- No REI, WTHIT, The One Probe, EMI or Patchouli integration.
