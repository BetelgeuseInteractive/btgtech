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
   1. `CoreFeature.register(modBus)`: shared infrastructure (the creative tab). Configuration registration joins it with the first config value (`SERVER`: WP-105), adding the `ModContainer` parameter.
   2. `MaterialFeature.register(modBus)`
   3. `EnergyFeature.register(modBus)`
   4. `MachineFeature.register(modBus)`
   5. `StorageFeature.register(modBus)`
   6. `LogisticsFeature.register(modBus)`
   7. `ToolFeature.register(modBus)`
2. On the physical client only, NeoForge constructs **`BTGTechClient`** (package `com.betelgeuseinteractive.btgtech.client`, so that it satisfies the `client` isolation rule of ADR 0018), which calls the `<Feature>ClientFeature.register(modBus)` methods in the same order. It also calls `DataGenerators.register(modBus)` (package `datagen`), because the data providers run only in the client data run (ADR 0050).
3. NeoForge fires `RegisterEvent`; every `DeferredRegister` registered above adds its entries.
4. Configs load; later lifecycle events reach the listeners added in step 1 and 2.

Registries must never be queried before registration has finished.

## 3. Contents of `core`

| Package | Content | Decision |
|---|---|---|
| `core` | Tier definition (`basic`, `advanced`, `elite`, `ultimate`) with its color ramp, configuration registration, the "BTG Tech" creative tab (`CoreCreativeModeTabs`), `CoreFeature` | ADR 0023, 0025, 0033, 0053 |
| `core.network` | Transport networks: per-level manager (`TransportNetworkManager`), tick and chunk hooks (`TransportNetworks`), endpoint capability caches; extension points `TransportNetworkType` and `TransportNodeBlock` | ADR 0031 |
| `core.network.logic` | Generic network graph (`NetworkGraph`, `Network`: nodes, merge/split, membership) | ADR 0031 |
| `core.packet` | Packet protocol version and shared codec helpers | ADR 0054 |
| `core.machine` | Machine framework: `MachineBlockEntity`, machine definitions, components, BTG energy storage class | ADR 0032, 0034 |
| `core.machine.logic` | Minecraft-independent machine logic: redstone mode, processing progress, active state, quantity formatting, gauge scaling | ADR 0018, 0035 |
| `core.machine.client` | Machine screen base and widget set: energy bar, progress arrow, redstone mode button, tooltip helpers | ADR 0044, 0045 |

`core.network` is reserved for transport networks (cables and pipes); packet code uses a different package (planning topic 11).

## 4. Placement Rules (summary)

- Shared code goes to `core` only with at least two users or when inherently cross-cutting (ADR 0023).
- `core` never imports feature packages; feature packages never form cycles.
- Client-only classes live only in `client` packages; nothing outside `client` imports them (ADR 0018), except the `datagen` package, which only client code reaches (ADR 0075).
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

**Adding a transport network (cables, pipes)**
1. Implement `TransportNetworkType<C>` in the owning feature: the endpoint capability and the transfer strategy, called once per network per server tick. Endpoints whose `handler()` is `null` are unloaded or do not expose the capability and are skipped. Use one type object per tier, so that tiers never connect (ADR 0028, 0030).
2. Let the node block implement `TransportNodeBlock` (network type, node sides and endpoint sides, all read from the block state) and call `TransportNetworks.refresh(level, pos)` on the server whenever the block is placed, removed or changes state.
3. Two nodes connect only when both connect toward each other and share the same type. Networks are rebuilt from block states as chunks load, so nothing is saved.
