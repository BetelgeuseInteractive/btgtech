# Space Technology Readiness

How v1 stays open for space technology without implementing it. Decisions: ADR [0042](../decisions/0042-multiblock-approach.md), [0069](../decisions/0069-closed-tier-set.md), [0070](../decisions/0070-dimension-agnostic-design.md), [0071](../decisions/0071-space-technology-extension-points.md).

## 1. Direction (not approved scope)

Rockets and launch pads (multiblocks), planets, oxygen and space suits, planet-specific ores, advanced energy sources, cross-dimension transport. The scope is planned in its own session when space work starts.

## 2. Rules in Force for v1

1. No dimension-specific checks (`Level.OVERWORLD` etc.); read environment properties instead.
2. Networks and machines assume nothing about their dimension.
3. Ores target dimensions through tags and biome modifiers.
4. Per-player state uses data attachments.
5. The tier set is closed at four; later progression uses materials, components and content.

## 3. Extension Points (documented only, no v1 code)

### 3.1 Multiblock Controller (`core.machine`)
- One controller block entity per structure owns the energy, item and fluid components.
- The structure pattern is data-defined (JSON) so new structures need no code.
- Validation runs when a member block is placed, removed or changed, never every tick.
- Member blocks forward capability queries to the controller.
- First candidates: large battery, boiler and turbine, launch pad.

### 3.2 Environment Factors
- Planet properties (gravity, oxygen, temperature, solar factor) are data-defined per dimension.
- Solar generation has one designated place where an environment factor will be applied ([energy.md §5](energy.md#5-extension-points)).

### 3.3 Cross-Dimension Transport (`core.network`)
- A new endpoint type that links networks across levels; the graph infrastructure stays unchanged.

### 3.4 Packages
- `space` feature package, created when space work is approved; may depend on `core`, `energy`, `logistics`.
- `api` package only if a concrete need appears.

## 4. Open Question for the Space Phase

Space station implementation: own runtime dimensions, a library mod (e.g. Infiniverse), or regions in one shared orbit dimension. Current lean: shared orbit dimension (lowest port risk). Decided by the first ADR of the space phase.
