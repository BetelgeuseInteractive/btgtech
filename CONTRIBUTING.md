# Contributing to BTG Tech

Thank you for helping improve BTG Tech. This document explains how contributions are licensed and how to submit them.

## Licensing of Contributions

- **Code, build scripts and data files** are licensed under the [Mozilla Public License 2.0](LICENSE). By submitting a pull request, you agree that your contribution is licensed under MPL-2.0.
- **Assets** (textures, models, sounds and other art) are All Rights Reserved by Betelgeuse Interactive ([LICENSE-ASSETS.md](LICENSE-ASSETS.md)). Art contributions are accepted only after prior agreement; open an issue first.

## Reporting Bugs

Open a GitHub issue and include the Minecraft, NeoForge and BTG Tech versions and the log file (`logs/latest.log`).

## Translations

BTG Tech ships in eight languages: `en_us` (default), `tr_tr`, `zh_cn`, `ru_ru`, `pt_br`, `es_es`, `de_de` and `fr_fr`. Corrections are welcome as pull requests.

- Edit the language file in `src/main/resources/assets/btgtech/lang/<language>.json`.
- `en_us` is produced by data generation; changes to English text go into the language provider in the `datagen` package, followed by `.\gradlew.bat runData`.
- Every language file must contain every `en_us` key; the build checks this.
- Block and item names are full strings in every language; do not build them from separate words.

## Code Changes

1. Set up the development environment as described in [environment setup](docs/development/environment-setup.md).
2. Follow the [coding standards](docs/development/coding-standards.md); run `.\gradlew.bat spotlessApply` before committing.
3. If generated content changes, run `.\gradlew.bat runData` and commit the output.
4. Make sure `.\gradlew.bat build` succeeds.
5. Open a pull request against `main` that describes what changed and how it was verified.

Changes that affect a recorded decision in [docs/decisions](docs/decisions/README.md) need to be discussed in an issue first.

## Commit Messages

Use [Conventional Commits](https://www.conventionalcommits.org/) in English, for example `feat: add energy cable network` or `fix: keep storage contents on break`. Allowed types: `feat`, `fix`, `docs`, `refactor`, `test`, `build`.
