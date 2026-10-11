# 0027. Soft tier bottleneck

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Every energy block has four tiers (ADR 0007). It had to be decided what happens when tiers are mixed. The target difficulty is "in the middle".

## Options Considered
- **A. No consequence:** tiers only change speed and capacity.
- **B. Voltage system** (GregTech/IC2 style): under-sized cables burn, machines explode; too punishing for the target difficulty.
- **C. Soft bottleneck:** a lower tier limits throughput; nothing is destroyed.

## Decision
Option C. Tier mismatches only limit throughput. No BTG Tech block is ever destroyed, damaged or made to explode by receiving energy from a higher tier.

## Consequences
- Upgrading cables is rewarded by higher throughput, never forced by destruction.
- Throughput values per tier are part of the balance table (planning topic 8).
