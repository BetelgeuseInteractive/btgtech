# 0051. Languages and translation

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
The owner requires 5–8 of the most widely used languages from the start, with English as the default (ADR 0002).

## Options Considered
- **A.** `en_us`, `tr_tr`, `zh_cn`, `ru_ru`, `pt_br`, `es_es`, `de_de`, `fr_fr`.
- **B.** As A, with `ja_jp` instead of `fr_fr`.

## Decision
- **Languages (option A):** `en_us` (default), `tr_tr`, `zh_cn`, `ru_ru`, `pt_br`, `es_es`, `de_de`, `fr_fr`.
- **Process:** the project drafts all translations; the owner reviews `tr_tr`; corrections for other languages come from community contributions (GitHub pull requests). A translation platform such as Crowdin is a later candidate.
- **Keys** follow vanilla conventions: `block.btgtech.<id>`, `item.btgtech.<id>`, `gui.btgtech.<name>`, `tooltip.btgtech.<name>`, `itemGroup.btgtech.<name>`, `advancements.btgtech.<name>.title` / `.description`.
- **Block and item names are full strings in every language**; names are never composed from a tier word and a machine word in code, because word order differs between languages.
- An automated test verifies that every language file contains every `en_us` key (planning topic 13).

## Consequences
- Adding content means adding its keys to all eight language files.
