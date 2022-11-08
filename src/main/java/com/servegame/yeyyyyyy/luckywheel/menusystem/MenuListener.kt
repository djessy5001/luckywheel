package com.servegame.yeyyyyyy.luckywheel.menusystem

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.core.Wheel
import com.servegame.yeyyyyyy.luckywheel.core.models.Loot
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable
import com.servegame.yeyyyyyy.luckywheel.extensions.currentLootTable
import com.servegame.yeyyyyyy.luckywheel.extensions.getColoredString
import com.servegame.yeyyyyyy.luckywheel.extensions.spawnParticles
import com.servegame.yeyyyyyy.luckywheel.extensions.toText
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

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.view.title.startsWith(messagesConfig.getColoredString("menu_title_prefix"))) event.isCancelled = true
        
        when (event.view.title) {
            MenuTitle.MainMenu.title -> onMainMenuClick(event)
            MenuTitle.LootTableListMenu.title -> onLootTableListMenuClick(event)
            MenuTitle.LootTableMenu.title -> onLootTableMenuClick(event)
            MenuTitle.WheelMenu.title -> onWheelMenuClick(event)
            MenuTitle.EditItemWeightMenu.title -> onEditItemWeightMenuClick(event)
        }
    }

    private fun onMainMenuClick(event: InventoryClickEvent) {
        when (event.currentItem?.itemMeta?.displayName) {
            MenuOptions.SpinTheWheel.option -> {
                Menu.openWheelGui(event.whoClicked as Player, lootTablesFileManager.getLootTable("default"))
            }
            MenuOptions.ShowLootTable.option -> {
                Menu.openLootTableListGui(event.whoClicked as Player)
            }
            MenuOptions.ExitMenu.option -> {
                event.whoClicked.closeInventory()
            }
        }
    }

    private fun onLootTableListMenuClick(event: InventoryClickEvent) {
        if (event.currentItem == null) return
        when (event.currentItem!!.itemMeta?.displayName) {
            MenuOptions.GoBack.option -> {
                Menu.openMainMenuGui(event.whoClicked as Player)
            }
            MenuOptions.ExitMenu.option -> {
                event.whoClicked.closeInventory()
            }
            else -> {
                val lootTables = lootTablesFileManager.getAllLootTables()
                val clickedLootTable = getClickedLootTable(event, lootTables)
                Menu.openLootTableGui(event.whoClicked as Player, clickedLootTable)
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
                    Menu.showEditItemWeightGui(player, event.currentItem!!)
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
        lootTablesFileManager.updateLootTable(lootTable)
        if (itemWasRemoved) Menu.openLootTableGui(event.whoClicked as Player, lootTable)
        return if (itemWasRemoved) "removed_item_from_loot_table" else "item_from_loot_table_could_not_be_removed"
    }

    private fun addItemToLootTable(lootTable: LootTable, event: InventoryClickEvent): String {
        val itemWasAdded = lootTable.add(Loot(event.currentItem as ItemStack, 1.0))
        lootTablesFileManager.updateLootTable(lootTable)
        if (itemWasAdded) Menu.openLootTableGui(event.whoClicked as Player, lootTable)
        return if (itemWasAdded) "added_item_to_loot_table" else "item_could_not_be_added_to_loot_table"
    }

    private fun onWheelMenuClick(event: InventoryClickEvent) {
        when (event.currentItem?.itemMeta?.displayName) {
            MenuOptions.SpinTheWheel.option -> {
                event.inventory.remove(event.currentItem!!)
                event.inventory.setItem(9 + 4, ItemStack(Material.SPECTRAL_ARROW))
                val player = event.whoClicked as Player
                val wheelMeta = player.getMetadata("luckywheel_wheel")
                val wheel = wheelMeta[0].value() as Wheel
                wheel.spin()
                player.spawnParticles(ParticleEffect(Particle.VILLAGER_HAPPY, player.location))
                player.playSound(player, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1f, 1f)
            }
            MenuOptions.GoBack.option -> {
                Menu.openMainMenuGui(event.whoClicked as Player)
            }
            MenuOptions.ExitMenu.option -> {
                event.whoClicked.closeInventory()
            }
        }
    }

    private fun onEditItemWeightMenuClick(event: InventoryClickEvent) {
        if (event.currentItem == null && (event.isLeftClick || event.isRightClick)) return

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
        Menu.showEditItemWeightGui(player, item)
    }

}