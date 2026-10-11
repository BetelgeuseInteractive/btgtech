# 0016. Java package root

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The Java package root appears in every class, in registration code and in the Maven coordinates of a future public API; changing it later is expensive. Java convention derives it from a domain the publisher controls. On 2026-10-11 Betelgeuse Interactive owned no domain but intends to acquire one.

## Options Considered
- **A. Domain-based:** `com.betelgeuseinteractive.btgtech`. Short and conventional; requires owning the domain to be fully legitimate (and to publish to Maven Central).
- **B. GitHub-based:** `io.github.betelgeuseinteractive.btgtech`. Ownership provable without a domain; long.
- **C. Mod ID only:** `btgtech`. Shortest; breaks Java convention.

## Decision
Option A. The package root is **`com.betelgeuseinteractive.btgtech`**, matching the domain `betelgeuseinteractive.com` that the owner intends to acquire. The Gradle `mod_group_id` uses the same value, as in the official MDK.

## Consequences
- If a different domain is acquired, a superseding ADR must be approved **before** the Phase 0 bootstrap; after code exists, renaming is costly.
- Maven coordinates for the public API are fixed in planning topic 12.
