# AprilPlus

AprilPlus is a Fabric mod for Minecraft snapshot `26w14a`, focused on Living Block gameplay improvements, client-side controls, and stability fixes.

![](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/built-with/java25_vector.svg)
![](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg)

![](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/unsupported/neoforge_vector.svg)
![](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/unsupported/forge_vector.svg)

[![](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)][Modrinth]
[![](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/curseforge_vector.svg)][CurseForge]

[![](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/donate/patreon-plural_vector.svg)][Patreon]

[![License](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)][License]

[Modrinth]: https://modrinth.com/mod/april-plus
[CurseForge]: https://www.curseforge.com/minecraft/mc-mods/april-plus
[Patreon]: https://www.patreon.com/gugle2308
[License]: https://spdx.org/licenses/LGPL-3.0-only.html

## Version and Compatibility

- Current version: `0.0.14`
- Minecraft: `26w14a` (`26.1.1-alpha.26.14.a`)
- Fabric Loader: `0.18.6+`
- Fabric API: `0.145.2+26w14a`

## Main Features

### Client Enhancements

- Free Camera
- Gamma Toggle
- Fast living-block group switching (numpad `0~6` and `.`)

### Interaction Improvements

- Double-click to quickly select nearby living blocks with the same item type
- `Shift + Right Click` to unselect the current target
- `Shift + Double Right Click` to batch-unselect nearby blocks in the same group and clear their group
- Furnaces do not smelt selected living blocks

### New in 0.0.14

- Added a `Shift` sequence-command mode
- While holding `Shift`, commands are queued instead of executed immediately, and queued actions run in order when blocks become idle

## Stability Fixes (Highlights)

- Fixed startup crashes (including early issue #2 related cases)
- Fixed crash caused by `X + <number>`
- Fixed crafter-related `NPE` and living-block tick `CCE` cases
- Fixed repeated disappearing issues when living blocks become air/item states
- Fixed an issue where living blocks could not be selected

> For the full history, see `CHANGE_LOG.md`.

## Default Keybindings

- `G`: Gamma Toggle
- `` ` ``: Free Camera
- Numpad `0`: Group None
- Numpad `1`: Group Red
- Numpad `2`: Group Blue
- Numpad `3`: Group Lime
- Numpad `4`: Group Yellow
- Numpad `5`: Group Purple
- Numpad `6`: Group Aqua
- Numpad `.`: Group All
