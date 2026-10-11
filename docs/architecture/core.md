# Core Architecture

How BTG Tech is assembled at startup and where code and state belong. Decisions: ADR [0018](../decisions/0018-feature-based-package-layout.md), [0021](../decisions/0021-per-feature-registration.md), [0022](../decisions/0022-entry-points-and-event-wiring.md), [0023](../decisions/0023-core-boundary-and-feature-dependencies.md), [0024](../decisions/0024-data-and-state-ownership.md), [0025](../decisions/0025-configuration-policy.md).

## 1. Building Blocks of a Feature

Every feature package (`energy`, `machine`, `storage`, `logistics`, `material`, `tool`) is assembled from the same parts. Only parts the feature actually needs are created.

| Part | Name pattern | Package | Purpose |
|---|---|---|---|
| Bootstrap | `<Feature>Feature` | `<feature>` | `register(IEventBus modBus)`: calls the holder classes' `register` methods and adds the feature's event listeners |
| Client bootstrap | `<Feature>ClientFeature` | `<feature>.client` | `register(IEventBus modBus)`: screens, renderers and other client-only listeners |
| Registry holders | `<Feature><RegistryType>` (e.g. `EnergyBlocks`, `EnergyItems`, `EnergyBlockEntityTypes`, `EnergyMenuTypes`) | `<feature>` | Own `DeferredRegister` + entries + `register(IEventBus modBus)` |
| Pure logic | Descriptive names | `<feature>.logic` | Minecraft-independent algorithms and calculations |
| Packets | `<Name>Payload` | `<feature>.packet` | Custom payload records (ADR 0054); client handlers live in `<feature>.client` |

## 2. Startup Sequence

1. NeoForge constructs **`BTGTech`** on both sides. Its constructor calls, in this order:
   1. `CoreFeature.register(modBus, modContainer)`: configuration registration and shared infrastructure.
   2. `MaterialFeature.register(modBus)`
   3. `EnergyFeature.register(modBus)`
   4. `MachineFeature.register(modBus)`
   5. `StorageFeature.register(modBus)`
   6. `LogisticsFeature.register(modBus)`
   7. `ToolFeature.register(modBus)`
2. On the physical client only, NeoForge constructs **`BTGTechClient`**, which calls the `<Feature>ClientFeature.register(modBus)` methods in the same order.
3. NeoForge fires `RegisterEvent`; every `DeferredRegister` registered above adds its entries.
4. Configs load; later lifecycle events reach the listeners added in step 1 and 2.

Registries must never be queried before registration has finished.

## 3. Contents of `core`

| Package | Content | Decision |
|---|---|---|
| `core` | Tier definition (`basic`, `advanced`, `elite`, `ultimate`), configuration registration, `CoreFeature` | ADR 0023, 0025, 0033 |
| `core.network` | Transport networks: per-level manager, tick hook, endpoint capability caches | ADR 0031 |
| `core.network.logic` | Generic network graph (nodes, merge/split, membership) | ADR 0031 |
| `core.packet` | Packet protocol version and shared codec helpers | ADR 0054 |
| `core.machine` | Machine framework: `MachineBlockEntity`, machine definitions, components, BTG energy storage class | ADR 0032, 0034 |

`core.network` is reserved for transport networks (cables and pipes); packet code uses a different package (planning topic 11).

## 4. Placement Rules (summary)

- Shared code goes to `core` only with at least two users or when inherently cross-cutting (ADR 0023).
- `core` never imports feature packages; feature packages never form cycles.
- Client-only classes live only in `client` packages; nothing outside `client` imports them (ADR 0018).
- `logic` packages never import `net.minecraft..` or `net.neoforged..` (ADR 0018).
- Features talk to each other through NeoForge capabilities where possible (ADR 0023).

## 5. State Ownership (summary)

Block entity → block state; data components → item-stack state; data attachments → only when no block entity of ours can own the state; the server is authoritative (ADR 0024).

## 6. Checklists

**Adding a new block to an existing feature**
1. Register the block in `<Feature>Blocks` and its item in `<Feature>Items`.
2. If it has a block entity, register the type in `<Feature>BlockEntityTypes`.
3. If it has a GUI, register the menu in `<Feature>MenuTypes` and the screen in `<Feature>ClientFeature`.
4. Add data generation (models, loot, recipes, tags, translations) as defined in planning topic 10.

**Adding a new feature package**
1. Requires an approved ADR (ADR 0018).
2. Create `<Feature>Feature` and add its call to `BTGTech` at the correct position in the startup order; update section 2 of this document.
3. Create `<Feature>ClientFeature` only if the feature has client code, and add its call to `BTGTechClient`.
