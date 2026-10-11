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

1. No class outside a `..client..` package depends on a `..client..` package.
2. No class in a `..logic..` package depends on `net.minecraft..` or `net.neoforged..`.
3. No class in `..core..` depends on a feature package.
4. Feature packages are free of cycles.
5. Only `..compat.jei..` references JEI; only `..compat.jade..` references Jade.
6. `@EventBusSubscriber` is not used; plugin annotations appear only in `..compat..`.

## 3. Required Data Checks

- Every key in `en_us.json` exists in each of the eight language files.
- Every registered block has a block state, model, loot table and translation.

## 4. Game Tests

- One empty platform structure template; each test places its blocks by code.
- Minimum coverage per feature: the core behavior of each block type (processing, distribution, transfer, contents kept on break, energy push, face toggling).

## 5. Rules

- Every public method in a `logic` package has a unit test.
- A bug fix comes with a test that fails without the fix, where technically feasible.
