package com.servegame.yeyyyyyy.luckywheel.menusystem

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.core.Wheel
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable
import com.servegame.yeyyyyyy.luckywheel.extensions.getColoredString
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue

class Menu {

    companion object {
        val messagesConfig = LuckyWheel.plugin.messagesFileManager.getConfig()

        fun openMainMenuGui(player: Player) {
            val inv = Bukkit.createInventory(null, 9, MenuTitle.MainMenu.title)

            addItem(MenuItem.spinTheWheel.copy(), inv)
            addItem(MenuItem.showLootTable.copy(), inv)
            addItem(MenuItem.exitMenu.copy(pos = inv.size - 1), inv)
            player.openInventory(inv)
        }

        fun openLootTableGui(player: Player, lootTable: LootTable) {
            val inv = Bukkit.createInventory(null, lootTable.inventorySize, MenuTitle.LootTableMenu.title)
            insertLootTableInInventory(lootTable, inv, player)
            val lootTableMeta = FixedMetadataValue(LuckyWheel.plugin, lootTable)
            player.setMetadata("luckywheel_loot_table", lootTableMeta)
            addItem(MenuItem.goBack.copy(pos = inv.size - 2), inv)
            addItem(MenuItem.exitMenu.copy(pos = inv.size - 1), inv)
            player.openInventory(inv)
        }

        fun openWheelGui(player: Player, lootTable: LootTable) {
            val inv = Bukkit.createInventory(null, 27, MenuTitle.WheelMenu.title)

            val wheel = Wheel(lootTable, inv, player)
            paintWheelRow(inv, wheel)
            val wheelMeta = FixedMetadataValue(LuckyWheel.plugin, wheel)
            player.setMetadata("luckywheel_wheel", wheelMeta)

            (9..17).forEach { pos -> addItem(MenuItem(Material.WHITE_STAINED_GLASS_PANE, "-", pos), inv) }
            addItem(MenuItem.spinTheWheel.copy(pos = 9 + 4), inv)
            addItem(MenuItem.goBack.copy(pos = inv.size - 2), inv)
            addItem(MenuItem.exitMenu.copy(pos = inv.size - 1), inv)
            player.openInventory(inv)
        }

        fun paintWheelRow(inv: Inventory, wheel: Wheel) {
            var index = 0
            wheel.itemRow.subList(0, 9).forEach { item -> inv.setItem(index++, item) }
        }


        private fun addItem(menuItem: MenuItem, inv: Inventory) {
            val (material, text, pos) = menuItem
            val item = ItemStack(material)
            setItemDisplayName(item, text)
            inv.setItem(pos, item)
        }

        private fun setItemDisplayName(spinItem: ItemStack, name: String) {
            val meta = spinItem.itemMeta!!
            meta.setDisplayName(name)
            spinItem.itemMeta = meta
        }

        private fun insertLootTableInInventory(
            lootTable: LootTable,
            inv: Inventory,
            player: Player
        ) {
            var index = 0
            lootTable.forEach { loot ->
                val meta = loot.item.itemMeta!!
                meta.lore = mutableListOf(
                    messagesConfig.getColoredString("common_probability") + lootTable.getProbabilityOfLootFormatted(loot),
                    if (player.hasPermission("luckywheel.loottables.edit")) messagesConfig.getColoredString("right_click_to_remove") else ""
                )
                loot.item.itemMeta = meta
                inv.setItem(index++, loot.item)
            }
        }

    }

}