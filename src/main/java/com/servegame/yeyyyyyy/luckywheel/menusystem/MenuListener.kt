package com.servegame.yeyyyyyy.luckywheel.menusystem

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.core.Wheel
import com.servegame.yeyyyyyy.luckywheel.core.models.Loot
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable
import com.servegame.yeyyyyyy.luckywheel.extensions.*
import com.servegame.yeyyyyyy.luckywheel.utils.ParticleEffect
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import java.lang.Double.max
import kotlin.math.round

class MenuListener : Listener {
    val messagesConfig = LuckyWheel.plugin.messagesFileManager.getConfig()
    val lootTablesFileManager = LuckyWheel.plugin.lootTablesFileManager
    val playersFileManager = LuckyWheel.plugin.playersFileManager

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.view.title.startsWith(messagesConfig.getColoredString("menu_title_prefix"))) event.isCancelled = true
        
        when (event.view.title) {
            MenuTitle.MainMenu.title -> onMainMenuClick(event)
            MenuTitle.WheelLootTableListMenu.title -> onWheelLootTableListMenuClick(event)
            MenuTitle.LootTableListMenu.title -> onLootTableListMenuClick(event)
            MenuTitle.LootTableMenu.title -> onLootTableMenuClick(event)
            MenuTitle.WheelMenu.title -> onWheelMenuClick(event)
            MenuTitle.EditItemWeightMenu.title -> onEditItemWeightMenuClick(event)
        }
    }

    private fun onMainMenuClick(event: InventoryClickEvent) {
        if (event.clickedInventory?.type == InventoryType.PLAYER) return

        when (event.currentItem?.itemMeta?.displayName) {
            MenuOptions.SpinTheWheel.option -> {
                Menu.openWheelLootTableListGui(event.whoClicked as Player)
            }
            MenuOptions.ShowLootTable.option -> {
                Menu.openLootTableListGui(event.whoClicked as Player)
            }
            MenuOptions.ExitMenu.option -> {
                event.whoClicked.closeInventory()
            }
        }
    }

    private fun onWheelLootTableListMenuClick(event: InventoryClickEvent) {
        if (event.clickedInventory?.type == InventoryType.PLAYER) return
        if (event.currentItem == null) return
        when (event.currentItem!!.itemMeta?.displayName) {
            MenuOptions.GoBack.option -> {
                Menu.openMainMenuGui(event.whoClicked as Player)
            }
            MenuOptions.ExitMenu.option -> {
                event.whoClicked.closeInventory()
            }
            else -> {
                if (event.isLeftClick) {
                    val player = event.whoClicked as Player
                    val lootTables = lootTablesFileManager.getAllLootTables()
                    val clickedLootTable = getClickedLootTable(event, lootTables)
                    val lastSpinDate = playersFileManager.getLastSpin(player.uniqueId, clickedLootTable.name)
                    if (!clickedLootTable.canSpin(lastSpinDate).first) {
                        if (!player.hasPermission("luckywheel.loottables.bypass_cooldown")) {
                            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1f, 0.8f)
                            return
                        } else {
                            player.sendMessage(messagesConfig.getColoredString("cooldown_bypassed"))
                            player.playSound(player.location, Sound.BLOCK_BEACON_POWER_SELECT, 2f, 1f)
                        }
                    }

                    Menu.openWheelGui(player, clickedLootTable)
                }
            }
        }
    }

    private fun onLootTableListMenuClick(event: InventoryClickEvent) {
        if (event.clickedInventory?.type == InventoryType.PLAYER) return
        if (event.currentItem == null) return
        when (event.currentItem!!.itemMeta?.displayName) {
            MenuOptions.GoBack.option -> {
                Menu.openMainMenuGui(event.whoClicked as Player)
            }
            MenuOptions.ExitMenu.option -> {
                event.whoClicked.closeInventory()
            }
            else -> {
                if (event.isLeftClick) {
                    val lootTables = lootTablesFileManager.getAllLootTables()
                    val clickedLootTable = getClickedLootTable(event, lootTables)
                    Menu.openLootTableGui(event.whoClicked as Player, clickedLootTable)
                } else if (event.isRightClick) {
                    val lootTables = lootTablesFileManager.getAllLootTables()
                    val clickedLootTable = getClickedLootTable(event, lootTables)
                    lootTablesFileManager.removeLootTable(clickedLootTable)
                    val player = event.whoClicked as Player
                    player.sendMessage(
                        messagesConfig.getColoredString("loot_table_was_removed")
                            .replace("{lootTable}", clickedLootTable.name)
                    )
                    Menu.openLootTableListGui(player)
                }
            }
        }
    }

    private fun getClickedLootTable(event: InventoryClickEvent, lootTables: List<LootTable>): LootTable {
        val index = Material.values().indexOfFirst { it == event.currentItem!!.type }
        val indexFirstItem = Material.values().indexOf(Material.WHITE_WOOL)
        val listIndex = index - indexFirstItem
        return lootTables[listIndex]
    }

    private fun onLootTableMenuClick(event: InventoryClickEvent) {
        when (event.currentItem?.itemMeta?.displayName) {
            MenuOptions.GoBack.option -> {
                Menu.openLootTableListGui(event.whoClicked as Player)
            }
            MenuOptions.ExitMenu.option -> {
                event.whoClicked.closeInventory()
            }
            else -> {
                handleOtherLootTableClick(event)
            }
        }
    }

    private fun handleOtherLootTableClick(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val inventory = event.inventory
        val helpPaperItem = inventory.getItem(inventory.size - 3)
        if (event.currentItem == null || event.currentItem == helpPaperItem) return
        if (player.hasPermission("luckywheel.loottables.edit")) {
            val lootTableName = player.currentLootTable()
            val lootTable: LootTable = lootTablesFileManager.getLootTable(lootTableName)
            var textResponse = ""
            if (event.clickedInventory?.type != InventoryType.PLAYER) {
                if (event.isLeftClick) {
                    val oldWeight = lootTable.getLoot(event.currentItem!!).weight
                    val oldWeightMeta = FixedMetadataValue(LuckyWheel.plugin, oldWeight)
                    player.setMetadata("luckywheel_old_weight", oldWeightMeta)
                    Menu.openEditItemWeightGui(player, event.currentItem!!)
                    textResponse = "introduce_weight"
                } else if (event.isRightClick) {
                    textResponse = removeItemFromLootTable(lootTable, event)
                }
            } else if (event.clickedInventory?.type == InventoryType.PLAYER && event.isLeftClick) {
                textResponse = addItemToLootTable(lootTable, event)
            }
            if (textResponse.isNotBlank()) {
                player.sendMessage(
                    messagesConfig.getColoredString(textResponse)
                        .replace("{item}", event.currentItem!!.toText()).replace("{lootTable}", lootTable.name)
                )
            }
        }
    }

    private fun removeItemFromLootTable(lootTable: LootTable, event: InventoryClickEvent): String {
        val itemWasRemoved = lootTable.remove(event.currentItem as ItemStack)
        val player = event.whoClicked as Player
        if (!itemWasRemoved) {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1f, 0.8f)
            return "item_from_loot_table_could_not_be_removed"
        }
        lootTablesFileManager.updateLootTable(lootTable)
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1f, 0.8f)
        Menu.openLootTableGui(event.whoClicked as Player, lootTable)
        return "removed_item_from_loot_table"
    }

    private fun addItemToLootTable(lootTable: LootTable, event: InventoryClickEvent): String {
        val player = event.whoClicked as Player
        if (isAlreadyInLootTable(lootTable, event)) return "item_already_in_loot_table"
        val itemWasAdded = lootTable.add(Loot(event.currentItem as ItemStack, 1.0))
        if (!itemWasAdded) {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1f, 0.8f)
            return "item_could_not_be_added_to_loot_table"
        }
        lootTablesFileManager.updateLootTable(lootTable)
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1f, 1.8f)
        Menu.openLootTableGui(event.whoClicked as Player, lootTable)
        return "added_item_to_loot_table"
    }

    private fun isAlreadyInLootTable(
        lootTable: LootTable,
        event: InventoryClickEvent,
        player: Player = event.whoClicked as Player
    ): Boolean {
        if (lootTable.any { loot -> loot.item.matches(event.currentItem!!) }) {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1f, 0.8f)
            return true
        }
        return false
    }

    private fun onWheelMenuClick(event: InventoryClickEvent) {
        if (event.clickedInventory?.type == InventoryType.PLAYER) return
        when (event.currentItem?.itemMeta?.displayName) {
            MenuOptions.GoBack.option -> {
                Menu.openWheelLootTableListGui(event.whoClicked as Player)
            }
            MenuOptions.ExitMenu.option -> {
                event.whoClicked.closeInventory()
            }
            MenuOptions.SpinTheWheel.option -> {
                event.inventory.remove(event.currentItem!!)
                event.inventory.setItem(9 + 4, ItemStack(Material.SPECTRAL_ARROW))
                val player = event.whoClicked as Player
                val wheelMeta = player.getMetadata("luckywheel_wheel")
                val wheel = wheelMeta[0].value() as Wheel
                wheel.spin()
                player.spawnParticles(ParticleEffect(Particle.VILLAGER_HAPPY, player.location))
                player.playSound(player.location, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1f, 1f)
            }
        }
    }

    private fun onEditItemWeightMenuClick(event: InventoryClickEvent) {
        if (event.currentItem == null && !(event.isLeftClick || event.isRightClick)) return
        if (event.clickedInventory?.type == InventoryType.PLAYER) return

        when (event.currentItem) {
            event.inventory.getItem(0) -> modifyItemWeight(event, -1.0)
            event.inventory.getItem(1) -> modifyItemWeight(event, -0.1)
            event.inventory.getItem(2) -> modifyItemWeight(event, -0.01)
            event.inventory.getItem(3) -> modifyItemWeight(event, -0.001)
            event.inventory.getItem(5) -> modifyItemWeight(event, 0.001)
            event.inventory.getItem(6) -> modifyItemWeight(event, 0.01)
            event.inventory.getItem(7) -> modifyItemWeight(event, 0.1)
            event.inventory.getItem(8) -> modifyItemWeight(event, 1.0)
            else -> {
                if (event.currentItem == event.inventory.getItem(4)) {
                    val player = event.whoClicked as Player
                    val lootTableName = player.currentLootTable()
                    if (event.isRightClick) {
                        val oldWeight = player.getMetadata("luckywheel_old_weight")[0].value() as Double
                        val lootTable = lootTablesFileManager.getLootTable(lootTableName)
                        lootTable.getLoot(event.currentItem!!).weight = oldWeight
                        lootTablesFileManager.updateLootTable(lootTable)
                        player.sendMessage(
                            messagesConfig.getColoredString("old_weight_reestablished")
                                .replace("{weight}", oldWeight.toString())
                        )
                    }
                    Menu.openLootTableGui(
                        player,
                        lootTablesFileManager.getLootTable(lootTableName)
                    )
                }
            }
        }

    }

    private fun modifyItemWeight(event: InventoryClickEvent, weightDiff: Double) {
        val player = event.whoClicked as Player
        val item = event.inventory.getItem(4)!!
        val lootTableName = player.currentLootTable()
        val lootTable = lootTablesFileManager.getLootTable(lootTableName)
        val loot = lootTable.getLoot(item)
        val newWeight = max(loot.weight + weightDiff, 0.0)
        loot.weight = round(newWeight * 1000) / 1000
        player.sendMessage(messagesConfig.getColoredString("new_weight").replace("{weight}", loot.weight.toString()))
        lootTablesFileManager.updateLootTable(lootTable)
        Menu.openEditItemWeightGui(player, item)
    }

}