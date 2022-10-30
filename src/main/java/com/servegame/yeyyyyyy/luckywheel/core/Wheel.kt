package com.servegame.yeyyyyyy.luckywheel.core

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.core.models.Loot
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable
import com.servegame.yeyyyyyy.luckywheel.menusystem.Menu
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask

class Wheel(val lootTable: LootTable, val menu: Menu, val inventory: Inventory) {
    val itemRow = mutableListOf<ItemStack>()
    var scheduler: BukkitScheduler = Bukkit.getScheduler()
    var task: BukkitTask? = null
    var counter = 0
    var prize: Pair<Loot, String>? = null

    init {
        fillItemRow()
    }

    fun spin() {
        prize = lootTable.getRandomLoot()
        task?.cancel()
        task = scheduler.runTaskTimerAsynchronously(LuckyWheel.plugin, Runnable {
            moveItems()
            menu.paintWheelRow(inventory, this)
        }, 0L, 2L)
    }

    fun moveItems() {
        itemRow.add(itemRow.removeFirst())
        counter++
        if (counter > 50 && itemRow[4].isSimilar(prize?.first?.item)) {
            task?.cancel()
            Bukkit.broadcastMessage("Juan has won " + prize?.first?.item?.amount + "x" + prize?.first?.item?.type + " which had " + prize?.second + " of probability")
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