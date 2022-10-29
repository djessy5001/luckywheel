package com.servegame.yeyyyyyy.luckywheel.core

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable
import com.servegame.yeyyyyyy.luckywheel.menusystem.Menu
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitScheduler

class Wheel(val lootTable: LootTable, val menu: Menu, val inventory: Inventory) {
    val itemRow = mutableListOf<ItemStack>()
    var scheduler: BukkitScheduler = Bukkit.getScheduler()
    var taskId: Int? = null
    var counter = 0

    init {
        fillItemRow()
    }

    fun spin() {
        lootTable.getRandomLoot()
        val task = scheduler.scheduleSyncRepeatingTask(LuckyWheel.plugin, {
            moveItems()
            menu.paintWheelRow(inventory, this)
        }, 0L, 5L)
    }

    fun moveItems() {
        itemRow.add(itemRow.removeFirst())
        counter++
        if (counter > 50) {
            scheduler.cancelTask(taskId!!)
        }
    }

    private fun fillItemRow() {
        itemRow.addAll(lootTable.getAllItems())
        while (itemRow.size < 9) {
            val fillerItem = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
            val meta = fillerItem.itemMeta!!
            meta.setDisplayName("-")
            fillerItem.itemMeta = meta
            itemRow.add(fillerItem)
        }
    }
}