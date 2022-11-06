package com.servegame.yeyyyyyy.luckywheel.core

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.core.models.Loot
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable
import com.servegame.yeyyyyyy.luckywheel.extensions.getColoredString
import com.servegame.yeyyyyyy.luckywheel.extensions.listItems
import com.servegame.yeyyyyyy.luckywheel.menusystem.Menu
import org.bukkit.*
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask

class Wheel(val lootTable: LootTable, val inventory: Inventory, val player: Player) {
    val messagesConfig = LuckyWheel.plugin.messagesFileManager.getConfig()
    val itemRow = mutableListOf<ItemStack>()
    private var scheduler: BukkitScheduler = Bukkit.getScheduler()
    private var task: BukkitTask? = null
    private var counter = 0
    var prize: Pair<Loot, String>? = null

    init {
        fillItemRow()
    }

    fun spin() {
        prize = lootTable.getRandomLoot()
        task?.cancel()
        task = scheduler.runTaskTimerAsynchronously(LuckyWheel.plugin, Runnable {
            moveItems()
            paintWheelRow()
        }, 0L, 2L)
    }

    private fun moveItems() {
        counter++
        if (counter <= 50) {
            itemRow.add(itemRow.removeFirst())
        } else {
            if (counter % 3 == 0) itemRow.add(itemRow.removeFirst())
        }
        if (counter > 60 && itemRow[4].isSimilar(prize?.first?.item)) {
            task?.cancel()
            scheduler.runTaskLater(LuckyWheel.plugin, Runnable {
                itemRow.replaceAll { if (it == itemRow[4]) it else ItemStack(Material.LIME_STAINED_GLASS_PANE) }
                paintWheelRow()
            }, 7L)
            givePrize()
        }
    }

    private fun paintWheelRow() {
        Menu.paintWheelRow(inventory, this)
    }

    private fun givePrize() {
        Bukkit.broadcastMessage(getWinMessage())
        showWinEffects()
        val notGivenItems = player.inventory.addItem(prize!!.first.item).map { item -> item.value }.toTypedArray()
        if (notGivenItems.isNotEmpty()) {
            val notStoredItems = player.enderChest.addItem(*notGivenItems).map { item -> item.value }.toTypedArray()
            if (notStoredItems.isEmpty()) {
                player.sendMessage(
                    messagesConfig.getColoredString("items_sent_to_enderchest")
                        .replace("{items}", notGivenItems.listItems())
                )
            } else {
                notStoredItems.forEach { itemStack ->
                    scheduler.runTask(LuckyWheel.plugin, Runnable {
                        player.world.dropItem(player.location, itemStack)
                    })
                }
                player.sendMessage(
                    messagesConfig.getColoredString("dropped_items")
                        .replace("{items}", notStoredItems.listItems())
                )
            }
        }
    }

    private fun showWinEffects() {
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
        scheduler.runTask(LuckyWheel.plugin, Runnable {
            val firework = player.world.spawn(player.location, Firework::class.java)
            firework.maxLife = 10
            val meta = firework.fireworkMeta
            meta.addEffect(
                FireworkEffect.builder().trail(true).withColor(Color.GREEN).withFlicker()
                    .with(FireworkEffect.Type.BURST)
                    .build()
            )
            firework.fireworkMeta = meta
        })
    }

    private fun getWinMessage() = messagesConfig.getColoredString("win_sentence")
        .replace("{name}", player.displayName)
        .replace("{amount}", prize!!.first.item.amount.toString())
        .replace("{item}", prize!!.first.item.type.name)
        .replace("{probability}", prize!!.second)

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