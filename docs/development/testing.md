# Testing

How BTG Tech is tested. Decision: ADR [0060](../decisions/0060-test-strategy.md).

## 1. Layers

| Layer | Location | Tool | Command |
|---|---|---|---|
| Unit tests | `src/test/java`, mirroring the tested package | JUnit | `.\gradlew.bat build` |
| Architecture tests | `src/test/java/.../architecture` | ArchUnit | `.\gradlew.bat build` |
| Data checks | `src/test/java/.../data` | JUnit | `.\gradlew.bat build` |
| Game tests | Test functions in the feature they test; `test_instance` / `test_environment` produced by data generation | NeoForge GameTest | `.\gradlew.bat runGameTestServer` |
| Manual | — | `runClient`, `runServer` | — |

## 2. Required Architecture Rules

1. No class outside a `..client..` package or the `datagen` package depends on a `..client..` package (ADR 0075).
2. No class in a `..logic..` package depends on `net.minecraft..` or `net.neoforged..`.
3. No class in `..core..` depends on a feature package.
4. Feature packages are free of cycles.
5. Only `..compat.jei..` references JEI; only `..compat.jade..` references Jade.
6. `@EventBusSubscriber` is not used; plugin annotations appear only in `..compat..`.
7. Only classes in `..client..` packages depend on the `datagen` package (ADR 0075).

## 3. Required Data Checks

- Every key in `en_us.json` exists in each of the eight language files.
- Every registered block has a block state, model, loot table and translation.

## 4. Game Tests

- One empty platform structure template, `btgtech:game_test_platform`: 8 × 4 × 8 blocks, a smooth stone floor and air above. It is a hand-made resource (`data/btgtech/structure/game_test_platform.nbt`, ADR 0060), because a compressed structure written on every data generation run could differ between operating systems and break the freshness check.
- Each test places its blocks by code, starting at `1, 1, 1` above the floor.
- Test functions live in the feature they test (`<Feature>GameTests`), registered with a `DeferredRegister` on the test function registry from the feature's bootstrap and listed in its `ALL` constant.
- `GameTestEntries` (data generation) produces one required test instance per function: default environment, the platform, at most 100 ticks.
- Minimum coverage per feature: the core behavior of each block type (processing, distribution, transfer, contents kept on break, energy push, face toggling).

## 5. Rules

- Every public method in a `logic` package has a unit test.
- A bug fix comes with a test that fails without the fix, where technically feasible.

## 6. Tooling

- JUnit 6 with ArchUnit's `archunit-junit6` module; its compatibility with JUnit 6 was verified in WP-002, so the JUnit 5 fallback of ADR 0061 is not used.
- Data checks parse JSON with Gson, a test-only dependency pinned to the version Minecraft ships; tests do not see the Minecraft libraries otherwise.
- Architecture rules about packages that do not exist yet allow an empty selection, so they apply as soon as the first class arrives.
- All versions are pinned in `gradle.properties`.

## 7. Continuous Integration

`.github/workflows/ci.yml` runs on every push and pull request (ADR 0066): `build`, then `runData` followed by a check that `src/generated` matches the committed output, then `runGameTestServer`.

- The game test server needs no Minecraft EULA acceptance (verified in WP-003).
- Minecraft's built-in `minecraft:always_pass` test runs with the mod's game tests, so the step passes before the mod has its own tests.
- `setup-gradle` caches Gradle and validates the wrapper jar against the checksums published by Gradle.
