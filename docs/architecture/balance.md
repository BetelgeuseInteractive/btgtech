# Balance Table

Default values for all tier-dependent numbers. Every value is a `SERVER` config default (ADR [0025](../decisions/0025-configuration-policy.md)). Scaling rules: ADR [0043](../decisions/0043-tier-scaling-and-balance-defaults.md). Units: t = tick (1/20 s), FE = energy, mB = millibucket (1 bucket = 1,000 mB).

## 1. Processing Machines

Applies to `electric_furnace`, `crusher`, `compressor`, `alloy_smelter`, `fluid_processor`. Recipe base duration defaults to 200 t; a recipe may override it (ADR 0036). Energy per operation at default duration: 4,000 FE on every tier.

| | Basic | Advanced | Elite | Ultimate |
|---|---|---|---|---|
| Duration of a 200 t recipe | 200 t | 100 t | 50 t | 25 t |
| Energy use while processing | 20 FE/t | 40 FE/t | 80 FE/t | 160 FE/t |
| Internal energy buffer | 10,000 FE | 40,000 FE | 160,000 FE | 640,000 FE |

## 2. Generators

| | Basic | Advanced | Elite | Ultimate | Notes |
|---|---|---|---|---|---|
| `solid_fuel_generator` output | 40 FE/t | 160 FE/t | 640 FE/t | 2,560 FE/t | Energy per fuel = burn time × 40 FE (coal: 64,000 FE) on every tier; higher tiers burn faster |
| `solar_panel` output (day, clear sky) | 4 FE/t | 16 FE/t | 64 FE/t | 256 FE/t | Rain: 50 %; night: 0 |
| `geothermal_generator` output | 80 FE/t | 320 FE/t | 1,280 FE/t | 5,120 FE/t | Lava: 200,000 FE per 1,000 mB (data map `btgtech:geothermal_fuels`) |
| `geothermal_generator` tank | 4,000 mB | 16,000 mB | 64,000 mB | 256,000 mB | |

## 3. Energy Storage and Transport

| | Basic | Advanced | Elite | Ultimate |
|---|---|---|---|---|
| `battery` capacity | 1,000,000 FE | 4,000,000 FE | 16,000,000 FE | 64,000,000 FE |
| `energy_cable` network throughput | 1,000 FE/t | 4,000 FE/t | 16,000 FE/t | 64,000 FE/t |

## 4. Item and Fluid Storage and Transport

| | Basic | Advanced | Elite | Ultimate |
|---|---|---|---|---|
| `item_storage_unit` capacity | 4,096 | 32,768 | 262,144 | 2,097,152 |
| `fluid_tank` capacity | 16,000 mB | 128,000 mB | 1,024,000 mB | 8,192,000 mB |
| `item_pipe` items per operation (every 10 t) | 4 | 16 | 64 | 256 |
| `fluid_pipe` throughput | 100 mB/t | 400 mB/t | 1,600 mB/t | 6,400 mB/t |

## 5. Ore Generation (Overworld)

| Ore | Height range (triangular, densest in the middle) | Vein size | Veins per chunk |
|---|---|---|---|
| Tin | Y −16 to 96 | 9 | 10 |
| Lead | Y −64 to 32 | 8 | 7 |

## 6. Processing Yields

- Crusher: raw ore → 1 dust + 33 % chance of a second dust; ore block → 2 dust.
- Exact recipe lists are produced by data generation (planning topic 10).

## 7. Change Log

| Date | Change | Approved by |
|---|---|---|
| 2026-10-11 | Initial defaults (ADR 0043) | Owner |
