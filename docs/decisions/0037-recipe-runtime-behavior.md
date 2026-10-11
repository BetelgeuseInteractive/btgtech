# 0037. Recipe runtime behavior

- **Status:** Accepted
- **Date:** 2026-10-11

## Context
Recipe lookups run on the server (`serverLevel.recipeAccess()`, `getRecipeFor`). By default NeoForge 26.1 does not send recipes to clients; custom types must be sent with `OnDatapackSyncEvent#sendRecipes` for recipe viewers and GUI previews to see them.

## Options Considered
Per-tick recipe lookup versus cached lookup; syncing BTG recipe types to clients versus server-only recipes; supporting the vanilla recipe book in machines versus leaving recipe display to recipe viewers.

## Decision
- **Caching:** each machine caches its last matching recipe and searches again only when its inputs change (consistent with ADR 0035). On datapack reload, cached recipes are treated as invalid and revalidated on the next tick.
- **Client sync:** all BTG recipe types are sent to clients through `OnDatapackSyncEvent#sendRecipes`; integration details are defined in planning topic 12.
- **Recipe book:** machines do not support the vanilla recipe book; recipe display is left to recipe viewers. If vanilla requires a recipe book category for each recipe, the simplest compliant option is determined during Phase 1 (not verified on 2026-10-11).

## Consequences
- Machines never perform recipe searches while idle.
- Client-received recipes are cleared on logout, as required by NeoForge.
