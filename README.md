# Moonrise Compats

Let’s make **Moonrise** work smoothly alongside other mods.

**Moonrise Compats** is a compatibility layer that adds targeted fixes/patches so mods that normally conflict with Moonrise can run together more reliably.


## What does this mod do?

It provides **compatibility support for Moonrise** with other mods.

That can include (depending on the patch/version):

- Fixing incompatibilities caused by mixins/overwrites
- Adjusting integration points so both mods can coexist
- Shipping small “bridge” patches when another mod expects vanilla behavior that Moonrise changes

> [!WARNING]
> This mod does **not** replace Moonrise.  
> It is meant to be installed **together with Moonrise**.

## Why does this exist?

Moonrise delivers optimizations we really like and rely on.  
Some mods weren’t built with Moonrise in mind, so conflicts happen. Instead of asking every mod author to handle it, we maintain compatibility patches here.

## Supported Moonrise versions

Moonrise Compats follows the **same base version as Moonrise**.

Example:

- `Moonrise-NeoForge 0.1.0-beta.15+2eae1b1`
- `MoonriseCompats 0.1.0-beta.15+2eae1b1.1`

The trailing suffix:

- `.1 ... .x`

means **Moonrise Compats patch revisions** (compatibility fixes / updates) for that exact Moonrise build.

So:

- `...+2eae1b1.1` = first Compats patch for Moonrise build `+2eae1b1`
- `...+2eae1b1.2` = second patch, etc.

## Installation

1. Install **Moonrise** (matching loader / platform).
2. Install **Moonrise Compats** with the matching base version.
3. Launch the game and check the log for compatibility messages (if provided by the mod).

## Compatibility & expectations

- This mod aims to be **safe**, but compatibility work can be highly mod-dependent.
- If you run a large modpack, always test updates in a separate instance first.

## Reporting issues

If something breaks, please include:

- Minecraft version
- Loader (NeoForge/Fabric/etc.)
- Exact versions of:
    - Moonrise
    - Moonrise Compats
    - The mod(s) that conflict
- `latest.log` (and crash report if present)
- Steps to reproduce (as short as possible)
