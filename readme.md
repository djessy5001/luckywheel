LuckyWheel (for spigot 1.16-1.21)
==========
By djessy5001
-------

https://www.spigotmc.org/resources/luckywheel-1-16-1-21.106505/

Have you ever wanted to reward your players for being active with random loots? LuckyWheel is the solution!!

### Description:

LuckyWheel is a spigot plugin that enables the players of the server to spin a wheel
that gives them a random item.

The LootTables of the LuckyWheel are customizable! You can:

- Change the items
- Modify the probability of dropping said item
- Change the cooldown for spinning the wheel
- Create new LootTables and erase them

You can also change the language of the plugin to spanish, by going to the config.yml
and changing

`locale: en`

with

`locale: es`

### Commands:

`/lw`: The main command. Shows a GUI with the LootTable and allows the user to spin the wheel.

`/lw create <name>`: Create a new LootTable with name `<name>` (requires permission `luckywheel.loottables.edit`)

### Permissions:

`luckywheel.command`: Allows the access to the main `/lw` command (default everyone)

`luckywheel.loottables.edit`: Enable the user to edit the LootTables (default admin)

`luckywheel.loottables.bypass_cooldown`: Enables the user to bypass the LootTables cooldown (default none)

### Cooldown:

`"cooldown": "DAILY"` in the loot-tables.json can be changed to other values to change the
cooldown time.

- DAILY - Every day (Monday, Tuesday, ...)
- FULL_DAY - Every 24 hours
- HOURLY - Every hour (8, 9, 10, ...)
- FULL_HOUR - Every 1 hour
- WEEKLY - Every week (not exactly 1 week)
- FULL_WEEK - Every 1 week

### Changelog:

v1.0.1:

- #2 Added backwards compatibility for spigot versions 1.16.x up to 1.19.x

v1.0.2:

- The LootTable Loots are now sorted by weight
- #6 Added missing permission checks before removing the LootTable
- #8 Fixed bug that made it impossible to add different enchanted books to the same LootTable
- Fixed wrong implementation of reflection for V1_19 class
- #4 Fixed NPE when using /luckywheel create with empty name
- Removed paper with instructions for regular players in LootTable Loot list

v1.0.3:

- Added compatibility with 1.20/1.20.1

v1.0.4:

- #11 Removed test command

v1.0.5:

- #12 Fixed potionData error on 1.20.5+ MC versions

v1.0.6:

- Added compatibility with 1.21