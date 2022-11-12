package com.servegame.yeyyyyyy.luckywheel.commands

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestCommand : CommandExecutor {
    private val logger = LuckyWheel.plugin.logger
    private val messagesConfig = LuckyWheel.plugin.messagesFileManager.getConfig()
    private val playersFileManager = LuckyWheel.plugin.playersFileManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
//        logger.info("total proba")
//        val lootTable = LuckyWheel.plugin.lootTablesFileManager.getLootTable("default")
//        logger.info(lootTable.getRandomLoot().toString())
//        var i = 0
//        Material.values().forEach { println(i++.toString() + " " + it) }
        val time = playersFileManager.getLastSpin((sender as Player).uniqueId, "default")
        Bukkit.broadcastMessage(time.toString())
        return true
    }
}