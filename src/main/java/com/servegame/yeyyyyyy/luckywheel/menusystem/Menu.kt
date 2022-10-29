package com.servegame.yeyyyyyy.luckywheel.menusystem

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.core.Wheel
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable
import com.servegame.yeyyyyyy.luckywheel.extensions.getColoredString
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class Menu : Listener {
    val messagesConfig = LuckyWheel.plugin.messagesFileManager.getConfig()
    val wheels = mutableListOf<Pair<String, Wheel>>()

    init {
        Bukkit.getPluginManager().registerEvents(this, LuckyWheel.plugin)
    }

    fun openMainMenuGui(player: Player) {
        val inv = Bukkit.createInventory(null, 9, MenuTitle.MainMenu.title)

        addSpinItem(inv)

        addExitDoor(inv)
        player.openInventory(inv)
    }

    fun openLootTableGui(player: Player, lootTable: LootTable) {
        val inv = Bukkit.createInventory(null, lootTable.inventorySize, MenuTitle.LootTableMenu.title)
        insertLootTableInInventory(lootTable, inv)

        addExitDoor(inv)
        player.openInventory(inv)
    }

    private fun openWheelGui(player: Player, lootTable: LootTable) {
        val inv = Bukkit.createInventory(null, 27, MenuTitle.WheelMenu.title)

        val wheel = Wheel(lootTable, this, inv)
        paintWheelRow(inv, wheel)

        addSpinItem(inv, 9 + 5)

        addExitDoor(inv)
        player.openInventory(inv)
    }

    fun paintWheelRow(inv: Inventory, wheel: Wheel) {
        var index = 0
        wheel.itemRow.subList(0, 9).forEach { item -> inv.setItem(index++, item) }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        when (event.view.title) {
            MenuTitle.MainMenu.title -> onMainMenuClick(event)
            MenuTitle.LootTableMenu.title -> onLootTableMenuClick(event)
            MenuTitle.WheelMenu.title -> onWheelMenuClick(event)
        }
    }

    private fun addSpinItem(inv: Inventory, pos: Int = 0) {
        val spinItem = ItemStack(Material.RECOVERY_COMPASS)
        val meta = spinItem.itemMeta!!
        meta.setDisplayName(messagesConfig.getColoredString("spin_the_wheel"))
        spinItem.itemMeta = meta
        inv.setItem(pos, spinItem)
    }

    private fun addExitDoor(inv: Inventory) {
        val door = ItemStack(Material.IRON_DOOR)
        val meta = door.itemMeta!!
        meta.setDisplayName(messagesConfig.getColoredString("exit_menu"))
        door.itemMeta = meta
        inv.setItem(inv.size - 1, door)
    }


    private fun insertLootTableInInventory(
        lootTable: LootTable,
        inv: Inventory
    ) {
        var index = 0
        lootTable.forEach { loot ->
            val meta = loot.item.itemMeta!!
            meta.lore = mutableListOf(
                messagesConfig.getColoredString("common_probability") + lootTable.getProbabilityOfLootFormatted(loot)
            )
            loot.item.itemMeta = meta
            inv.setItem(index++, loot.item)
        }
    }

    private fun onMainMenuClick(event: InventoryClickEvent) {
        event.isCancelled = true
        if (event.currentItem?.itemMeta?.displayName == messagesConfig.getColoredString("spin_the_wheel")) {
            openWheelGui(event.whoClicked as Player, LootTable())
        }
        return
    }

    private fun onLootTableMenuClick(event: InventoryClickEvent) {
        event.isCancelled = true
        if (event.currentItem?.itemMeta?.displayName == messagesConfig.getColoredString("exit_menu")) {
            event.whoClicked.closeInventory()
        }
        return
    }

    private fun onWheelMenuClick(event: InventoryClickEvent) {
        event.isCancelled = true
        if (event.currentItem?.itemMeta?.displayName == messagesConfig.getColoredString("spin_the_wheel")) {
            val meta = event.currentItem!!.itemMeta!!
            var userWheel: Wheel? = null
            wheels.forEach { (id, wheel) -> if (id == meta.lore?.get(0)) userWheel = wheel }
            userWheel?.spin()
        }
        return
    }


}